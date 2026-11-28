package et.tms.api.web;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class AuditLogInterceptor implements HandlerInterceptor {
    private static final Logger log =
            LoggerFactory.getLogger(AuditLogInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        log.info("TMS API call: {} {}",
                request.getMethod(), request.getRequestURI());
        return true; // continue to the controller
    }
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        log.info("TMS API response: {}", response.getStatus());
    }
}
