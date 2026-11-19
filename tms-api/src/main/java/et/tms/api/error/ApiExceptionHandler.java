package et.tms.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    @ExceptionHandler(TmsDatabaseException.class)
    public ProblemDetail handleDatabaseException(TmsDatabaseException ex){
        log.error("Enrollment database is unavailable ", ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "The enrollment database is temporarily unavailable."
        );

        problem.setTitle("Database unavailable");

        return problem;
    }
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex){
        log.error("Unexpected error occurred ", ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An Unexpected error occurred "
        );

        problem.setTitle("Internal server error");

        return problem;
    }


    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ProblemDetail> handleOptimisticLock(
            ObjectOptimisticLockingFailureException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Student was modified. Fetch the latest version and retry."
        );

        problem.setTitle("Concurrent modification");

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(problem);
    }


}