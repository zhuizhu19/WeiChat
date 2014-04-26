/*
 * Copyright 2012 NJUT  qixiaobo. All rights reserved.
 */
package org.liyou.qixiaobo.execptions;

/**
 * Resource404Exception.java
 * <p>
 * 在页面找不到的时候抛出此异常
 * </p>
 *
 * @author Qixiaobo-win8
 */
public class Resource404Exception extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Resource404Exception () {
        super();

    }

    public Resource404Exception (String message, Throwable cause) {
        super(message, cause);

    }

    public Resource404Exception (String message) {
        super(message);

    }

    public Resource404Exception (Throwable cause) {
        super(cause);

    }

}
