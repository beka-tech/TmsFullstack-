import java.math.BigDecimal;
import java.math.RoundingMode;

public record Quiz (String title, int correctAnswers,  int totalQuestions)
implements Gradable{

    @Override
    public BigDecimal calculateGrade(){
        if (totalQuestions == 0 ) return BigDecimal.ZERO;

        // Percentage = (Correct Answers × 100) ÷ Total Questions
        // return calculateGrade values
        return BigDecimal.valueOf(correctAnswers)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalQuestions),2,RoundingMode.HALF_UP);
    }
}