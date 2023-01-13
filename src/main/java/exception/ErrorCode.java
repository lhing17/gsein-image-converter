package exception;

public enum ErrorCode {
    SOURCE_FILE_NOT_FOUND(1001, "源文件不存在"),
    UNKNOWN_FORMAT(1002, "未知的文件格式"),
    SOURCE_IMAGE_LIST_EMPTY(1003, "源图片列表为空"),
    TARGET_PATH_EMPTY(1004, "目标路径为空"),
    TARGET_PATH_NOT_EXIST(1005, "目标路径不存在"),
    TARGET_PATH_NOT_DIRECTORY(1006, "目标路径不是文件夹"),
    TARGET_PATH_NOT_WRITEABLE(1007, "目标路径不可写"),
    TARGET_FORMAT_EMPTY(1008, "目标格式为空"),

    CONVERT_FAILED(2001, "转换失败"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
