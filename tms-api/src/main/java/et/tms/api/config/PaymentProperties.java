package et.tms.api.config;
import jakarta.validation.constraints.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@ConfigurationProperties(prefix = "tms.payments")
public record PaymentProperties(
        @NotBlank String gatewayUrl,
        @DecimalMin("100") @DecimalMax("10000") BigDecimal maxDepositBirr) {


}
