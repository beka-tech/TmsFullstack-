package et.tms.api.entity;
import java.math.BigDecimal;
public class Assessment {
    private Long id;
    private String title;
    private BigDecimal maxScore;
    private BigDecimal weight; // share of the final grade, e.g. 0.30 for 30%
    private Course course; // the owning course
}