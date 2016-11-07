package mts.delhivery.com.mts.domain;


import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.util.Tracer;

public class ErrorD {


    private final String TAG = Config.logger + ErrorD.class.getSimpleName();
    private String message;
    private String error_code;

    public static String SERVER_ERROR = "Server Error";
    public static String JSON_ERROR = "Json Parcing Error";
    public static String SERVER_ERROR_CODE = "500";
    public static String NO_INTERNET = "501";
    public static String JSON_ERROR_CODE = "502";
    public static String SESSION_INVALID = "403";
    public static String SESSION_INVALID_DESC = "jwt expired";
    public static String SERVER_EPMTY_ERROR_CODE = "420";

    public ErrorD(String message, String errorCode) {
        Tracer.debug(TAG, "[ErrorDelhivery] _ " + "error code="+errorCode);
        this.message = message;
        if(errorCode != null) {
            this.error_code = errorCode;
        }else{
            this.error_code = SERVER_EPMTY_ERROR_CODE;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getErrorCode() {
        return error_code;
    }


    public static ErrorD getServerError() {
        return new ErrorD(SERVER_ERROR, SERVER_ERROR_CODE);
    }

    public static ErrorD getJsonrError() {
        return new ErrorD(JSON_ERROR, JSON_ERROR_CODE);
    }
}
