public class TmsDatabaseException extends RuntimeException {

    private final String operation;

    public TmsDatabaseException (String operation, String message){
        super (message);
        this.operation = operation;
    }

    public TmsDatabaseException(String operation, String message, Throwable  cause){
        super(message, cause);
        this.operation = operation;
    }
    public String getOperation() {return operation; }
}
