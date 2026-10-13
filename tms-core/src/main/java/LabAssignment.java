import java.math.BigDecimal;

public record LabAssignment (String title,
                             BigDecimal functionalityScore,
                             BigDecimal codeQualityScore)
implements Gradable {
    private static final BigDecimal FUNCTIONALITY_WEIGHT = new BigDecimal ("0.7");
    private static final BigDecimal QUALITY_WEIGHT = new BigDecimal ("0.3");

    @Override
    public BigDecimal calculateGrade(){
        // 70% functionality 30% code quality
        // Final Score = (Functionality Score × Functionality Weight)
//             + (Code Quality Score × Quality Weight)
        // return calculateGrade values
        return functionalityScore.multiply(FUNCTIONALITY_WEIGHT)
                .add(codeQualityScore.multiply(QUALITY_WEIGHT));
    }
}