package et.tms.api.report;

import java.math.BigDecimal;

public record StudentSummary(
        Long id,
        String name,
        BigDecimal gpa
) {

}
