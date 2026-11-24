package et.tms.api.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String code; // natural key: "CS-101"
    @Column(nullable = false, length = 200)
    private String title;
    @Column(name = "max_capacity",nullable = false)
    private int maxCapacity;
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();
    public Course() {}
    public Course(String code, String title, int capacity) {
        this.code = code;
        this.title = title;
        this.maxCapacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getmaxCapacity() {
        return maxCapacity;
    }

    public void setmaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
    // getters and setters ...
}