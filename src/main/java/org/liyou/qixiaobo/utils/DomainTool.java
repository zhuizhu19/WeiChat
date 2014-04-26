/*
 * Copyright 2012 NJUT  qixiaobo. All rights reserved.
 */
package org.liyou.qixiaobo.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * DomainTool.java
 *
 * @author Qixiaobo-win8
 */
public class DomainTool {
    private static Properties context;

    static {
        Resource r = new ClassPathResource(
                "context.properties");
        try {
            context = PropertyUtils.readFromResource(r);
        } catch (IOException e) {
            context = new Properties();
        }
    }

    public static String info (String key) {
        return context.getProperty(key);
    }
}
