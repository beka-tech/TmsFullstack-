
package et.tms.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "max_score", nullable = false,
            precision = 10, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "weight", nullable = false,
            precision = 5, scale = 4)
    private BigDecimal weight;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    protected Assessment() {
    }

    public Assessment(String title, BigDecimal maxScore, BigDecimal weight, Course course) {
        this.title = title;
        this.maxScore = maxScore;
        this.weight = weight;
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
