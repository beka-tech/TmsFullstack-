package et.tms.api.exception;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class StudentNotFoundException
        extends RuntimeException {

    public StudentNotFoundException(long id) {
        super("Student not found with id: " + id);
    }


//    @ExceptionHandler(OptimisticLockingFailureException.class)
//    public ProblemDetail handleOptimisticLock(
//            OptimisticLockingFailureException ex
//    ) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.CONFLICT,
//                "This student was changed by someone else. " +
//                        "Reload and try again."
//        );
//    }
}