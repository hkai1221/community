package life.hk1221.community.exception;

import life.hk1221.community.advice.ICustomizeErrorCode;


public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public  String getMessage(){
        return message;
    }



}
