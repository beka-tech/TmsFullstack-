
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
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final String HEADER = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {

        String id = Optional.ofNullable(req.getHeader(HEADER))
                .filter(h -> h.matches("[A-Za-z0-9-]{1,64}"))
                .orElseGet(() -> UUID.randomUUID().toString());

        long start = System.nanoTime();

        MDC.put("correlationId", id);

        try {
            res.setHeader(HEADER, id);

            chain.doFilter(req, res);

        } finally {
            try {
                long ms = (System.nanoTime() - start) / 1_000_000;

                log.info("{} {} -> {} in {} ms",
                        req.getMethod(),
                        req.getRequestURI(),
                        res.getStatus(),
                        ms
                );

            } finally {
                MDC.remove("correlationId");
            }
        }
    }
}
