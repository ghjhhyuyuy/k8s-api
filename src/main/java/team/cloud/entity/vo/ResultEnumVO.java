package team.cloud.entity.vo;

/**
 * Description:
 *
 * @author tzw
 * CreateTime 12:54 2019/5/18
 **/
public enum ResultEnumVO {

    UNKNOW_ERROR(-1,"未知错误"),
    SUCCESS(0,"成功"),
    FAIL(1,"失败"),
    PARAM_ERROR(10001,"参数错误"),
    LOGIN_ERROR(10002,"参数错误"),
    RESOURCE_NOT_FOUND(10003,"资源不存在"),
    RESOURCE_EXIST(10004,"资源已存在"),
    TIME_OUT(10005,"请求超时"),
    ;
    private int code;

    private String msg;

    ResultEnumVO(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
