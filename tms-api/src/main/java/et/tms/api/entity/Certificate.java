package et.tms.api.entity;
import java.time.Instant;
public class Certificate {
    private Long id;
    private String serialNumber; // natural key: printed on the certificate
    private Instant issuedAt = Instant.now();
    private Student student;
    private Course course;
}