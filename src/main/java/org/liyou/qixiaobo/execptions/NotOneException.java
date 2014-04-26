/**
 * Copyright By Nanjing Fujitsu Nanda Software Technology Co., Ltd
 * 下午4:37:54
 * NotOneException.java
 *
 */
package org.liyou.qixiaobo.execptions;

/**
 * @author qixb.fnst
 */
public class NotOneException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1296710339626448346L;

    /**
     *
     */
    public NotOneException () {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public NotOneException (String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public NotOneException (String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public NotOneException (Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
