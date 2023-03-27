package com.rio.report.common;


/**
 * prefix
 * 0 = 공통
 * 1 = 사용자
 * 2 = 메뉴
 * 3 = 공정
 * 4 = 설비
 */
public enum EnumResult {

    //@formatter:off
    SUCCESS("0000", "success."),
    FAIL("9999", "fail")

    ;

    //@formatter:on


    private String cd;
    private String msg;

    EnumResult(String cd, String msg) {
        this.cd = cd;
        this.msg = msg;
    }

    /**
     * @return the cd
     */
    public String getCd() {
        return cd;
    }

    /**
     * @param cd the cd to set
     */
    public void setCd(String cd) {
        this.cd = cd;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }


}
