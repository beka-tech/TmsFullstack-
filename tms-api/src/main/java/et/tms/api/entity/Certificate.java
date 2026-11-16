
package et.tms.api.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_number",
            nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    protected Certificate() {
    }

    // Add constructors and getters as needed.


    public Certificate(String serialNumber, Student student, Course course) {
        this.serialNumber = serialNumber;
        this.student = student;
        this.course = course;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
