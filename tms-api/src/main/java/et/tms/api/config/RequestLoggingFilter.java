package et.tms.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // outermost: wraps security too
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String HEADER = "X-Correlation-Id";
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String id = Optional.ofNullable(req.getHeader(HEADER))
                .filter(h -> h.matches("[A-Za-z0-9-]{1,64}")) // reject log-injection junk
                .orElseGet(() -> UUID.randomUUID().toString());
        long start = System.nanoTime();
        MDC.put("correlationId", id); // included in every log line of this request
        res.setHeader(HEADER, id); // BEFORE doFilter: response not committed yet
        try {
            chain.doFilter(req, res); // the whole rest of the request happens in here
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            log.info("{} {} -> {} in {} ms {}  ", req.getMethod(), req.getRequestURI(), res.getStatus(),req.getReader(), ms) ;
            MDC.remove("correlationId"); // never leak into the next request on this thread
        }
    }
}

