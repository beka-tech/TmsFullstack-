package et.tms.api.error;

import et.tms.api.exception.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice

public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
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


    @ExceptionHandler(StudentNotFoundException.class)
    public ProblemDetail handleNotFound(
            StudentNotFoundException ex
    ) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        // Spring has already prepared a 400 ("Invalid request content.")
        ProblemDetail problem = ex.getBody();
        problem.setTitle("Validation failed");
        problem.setDetail("One or more fields are invalid.");
        problem.setProperty("errors", fieldErrors(ex));
        return handleExceptionInternal(ex, problem, headers, status, request);
    }
    private static Map<String, List<String>> fieldErrors(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new TreeMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.computeIfAbsent(error.getField(), field -> new ArrayList<>())
                    .add(error.getDefaultMessage());
        }
        return errors;
    }


}