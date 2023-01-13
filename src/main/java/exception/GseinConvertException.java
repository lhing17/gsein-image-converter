package exception;

public class GseinConvertException extends RuntimeException {
    private final int code;
    private final String message;

    public GseinConvertException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public GseinConvertException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
