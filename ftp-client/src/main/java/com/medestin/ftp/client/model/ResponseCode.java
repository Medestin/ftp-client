package com.medestin.ftp.client.model;

public enum ResponseCode {

    /**
     * 220 : Service ready for new user
     */
    READY(220),
    /**
     * 229 : Entering Extended Passive Mode (|||port|).
     */
    ENTERED_EPSV(229),
    /**
     * 230 : User logged in, proceed
     */
    LOGGED_IN(230),
    /**
     * 257 : "PATHNAME" created
     */
    PATHNAME_CREATED(257),
    /**
     * 331 : User okay, need password
     */
    NEED_PASSWORD(331);

    ResponseCode(int code) {
        this.code = code;
    }

    private final int code;

    public int code() {
        return code;
    }

    public ResponseCode getByCode(int code) {
        for (ResponseCode value : ResponseCode.values()) {
            if(value.code == code) {
                return value;
            }
        }
        return null;
    }

}
