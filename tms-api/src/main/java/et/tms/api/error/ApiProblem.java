package et.tms.api.error;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
/** Small factory for RFC 9457 errors; ApiExceptionHandler renders them. */
public final class ApiProblem {
    private ApiProblem() {}
    public static ErrorResponseException notFound(String title,
                                                  String detail) {
        return of(HttpStatus.NOT_FOUND, title, detail);
    }
    public static ErrorResponseException conflict(String title,
                                                  String detail) {
        return of(HttpStatus.CONFLICT, title, detail);
    }
    private static ErrorResponseException of(HttpStatus status,
                                             String title, String detail) {
        ErrorResponseException problem = new ErrorResponseException(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        return problem;
    }
}