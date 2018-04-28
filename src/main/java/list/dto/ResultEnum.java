package list.dto;

/**
 * 错误码及错误信息
 */
public enum ResultEnum {
    RC_0000000("0000000", "成功"),
    RC_0000001("0000001", "没有此书"),

    // ===============
    RC_0401001("0401001", "系统异常");


    private String code;
    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResultEnum getEnum(String code) {
        ResultEnum[] resultEnums = values();
        int size = resultEnums.length;

        for (int i = 0; i < size; ++i) {
            ResultEnum resultEnum = resultEnums[i];
            if (resultEnum.code.equals(code)) {
                return resultEnum;
            }
        }
        return null;
    }
}
