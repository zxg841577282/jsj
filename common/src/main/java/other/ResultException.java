package other;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * 自定义异常的封装类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ResultException extends RuntimeException{


    private static String commonError = "对不起 出现内部错误"; // 通用错误描述

    private String code; // 错误码

    private String errMessage; // 错误信息

    private String trackId; // trackId

    public ResultException(String errMessage){
        this("500",errMessage);
    }

    public ResultException(String code, String errMessage) {
        this.code = code;
        this.errMessage = errMessage;
        createTrackId();
    }


    private void createTrackId(){
        this.trackId = UUID.randomUUID().toString();
    }


}
