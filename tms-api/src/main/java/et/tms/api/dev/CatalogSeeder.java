package et.tms.api.dev;
import et.tms.api.entity.Course;
import et.tms.api.repository.CourseRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@Profile("!prod") // never seeds production
@Order(2) // after M5's DevDataSeeder (@Order(1))
public class CatalogSeeder implements ApplicationRunner {
    private record Entry(String code, String title, int maxCapacity) {}
    private static final List<Entry> CATALOG = List.of(
            // the three courses M5 already seeds
            new Entry("CS-101", "Introduction to Computer Science", 30),
            new Entry("CS-201", "Data Structures and Algorithms", 25),
            new Entry("MAT-101", "Calculus I", 40),
            // the rest of the catalog
            new Entry("CS-102", "Java Fundamentals", 30),
            new Entry("CS-103", "Git and Collaborative Workflows", 25),
            new Entry("CS-202", "Object-Oriented Design with Java", 28),
            new Entry("CS-203", "Concurrency and Virtual Threads", 24),
            new Entry("WEB-101", "HTML, CSS and Responsive Design", 30),
            new Entry("WEB-102", "TypeScript Essentials", 30),
            new Entry("WEB-201", "Angular Fundamentals", 26),
            new Entry("WEB-301", "Angular Advanced Patterns", 24),
            new Entry("API-201", "Spring Boot Essentials", 28),
            new Entry("API-202", "Building REST APIs with Spring Boot", 28),
            new Entry("API-301", "Advanced Web API Patterns", 24),
            new Entry("DB-101", "Database Design Foundations", 30),
            new Entry("DB-201", "Spring Data JPA and PostgreSQL", 28),
            new Entry("DB-202", "Advanced SQL and Indexing", 26),
            new Entry("SEC-201", "Security and Authentication", 20),
            new Entry("TST-201", "Testing and Quality Assurance", 22),
            new Entry("ARC-101", "Software Architecture Patterns", 22),
            new Entry("ARC-201", "Cloud-Native Architecture", 22),
            new Entry("OPS-101", "DevOps Foundations", 24),
            new Entry("OPS-201", "Continuous Delivery Pipelines", 22),
            new Entry("UX-101", "UX Research and Wireframing", 24),
            // tiny on purpose: Session 3 fills it to capacity
            new Entry("SEM-101", "Capstone Showcase Seminar", 2));
    private final CourseRepository courses;
    public CatalogSeeder(CourseRepository courses) {
        this.courses = courses;
    }
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // One of the few places where loading a small table whole is fine.
        Set<String> present = courses.findAll().stream()
                .map(Course::getCode)
                .collect(Collectors.toSet());
        List<Course> missing = CATALOG.stream()
                .filter(entry -> !present.contains(entry.code()))
                .map(entry -> new Course(entry.code(), entry.title(),
                        entry.maxCapacity()))
                .toList();
        courses.saveAll(missing); // idempotent: re-runs add nothing
    }
}