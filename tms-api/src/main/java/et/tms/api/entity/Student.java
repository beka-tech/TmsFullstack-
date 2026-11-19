package et.tms.api.entity;
import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "students")
@EntityListeners(AuditingEntityListener.class)
public class Student {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // surrogate key: used by foreign keys
    @Column(name = "registration_number", nullable = false, length = 20)
    private String registrationNumber; // natural key: human-readable
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal gpa;
    @Column(nullable = false)
    private boolean active = true;
    @OneToMany(mappedBy = "student") // the other side owns the foreign key
    private List<Enrollment> enrollments = new ArrayList<>();

    @LastModifiedDate
    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Version
    @Column(name = "version" , nullable = false)
    private long version;

    protected Student() {} // required by JPA
    public Student(String registrationNumber, String name, BigDecimal gpa) {
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.gpa = gpa;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    // getters and setters ...

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public long getVersion() {
        return version;
    }


}


/* audit stamp
Hibernate automatically maintains last_updated when a student changes.
Hibernate increments version on a successful update.
Two staff members cannot silently overwrite each other's changes.
An outdated update receives HTTP 409 Conflict.
API clients receive the version but not the internal audit timestamp.
* */