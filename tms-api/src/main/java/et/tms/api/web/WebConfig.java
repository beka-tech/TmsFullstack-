package et.tms.api.web;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuditLogInterceptor auditLog;
    public WebConfig(AuditLogInterceptor auditLog) {
        this.auditLog = auditLog;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditLog).addPathPatterns("/api/**");
    }
}