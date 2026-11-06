package et.tms.api.error;

public class TmsDatabaseException extends RuntimeException {
    public TmsDatabaseException(String message) {
        super(message);
    }
}
