/*
 * Copyright 2012 NJUT  qixiaobo. All rights reserved.
 */
package org.liyou.qixiaobo.utils;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * PropertyUtils.java
 *
 * @author Qixiaobo-win8
 */
public class PropertyUtils extends org.apache.commons.beanutils.PropertyUtils {

    public static Properties readFromResource (Resource resource)
            throws IOException {
        Properties pro = new Properties();
        InputStream ins = null;
        ins = resource.getInputStream();
        if (null != ins) {
            pro.load(ins);
        } else {
            throw new IOException("inputstream of resource is null！");
        }
        return pro;
    }
}
