/*
 * Copyright 2012 NJUT  qixiaobo. All rights reserved.
 */
package org.liyou.qixiaobo.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BaseController.java
 * <p>
 * 所有的Controller必须继承此类，提供了一些常用的方法
 * </p>
 *
 * @author Qixiaobo-win8
 */
public class BaseController extends CookieUtils {
    protected final String REDIRECT = "redirect:";
    protected final String FORWARD = "forward:";
    protected final String LASTACCESS = "lastAccess";
    protected final String LOGIN = "login";
    protected final String CURRENTUSER = "currentUser";
    protected final String USERID = "userId";
    protected final String INLINE = "inline";
    protected final String ATTACHMENT = "attachment";
    private final String DEFALULT_CONTENT_TYPE = "application/octet-stream";
    private static DateFormat df;
    public static String WEB_REAL_PATH;

    static {
        df = new SimpleDateFormat ("yyyy-MM-dd");
    }

    public String redirect (String url) {
        StringBuilder sb = new StringBuilder ();
        sb.append (REDIRECT);
        sb.append (url);
        return sb.toString ();
    }

    public String forward (String url) {
        StringBuilder sb = new StringBuilder ();
        sb.append (FORWARD);
        sb.append (url);
        return sb.toString ();
    }

    /**
     * 密码加密
     *
     * @param password
     * @return
     */
    public String encrypt (String password) {
        return StringUtils.encrypt (password);
    }

    /**
     * 密码解密
     *
     * @param data
     * @return
     */
    public String decrypt (String data) {
        return StringUtils.decrypt (data);
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin (HttpSession session) {
        if (session.getAttribute (LOGIN) != null
                && session.getAttribute (LOGIN).equals ("true")) {
            return true;
        }
        return false;
    }

    /**
     * 设置登录
     */
    public void setLoginState (HttpSession session, String userName,
                               String userId, Date date) {
        session.setAttribute (LOGIN, "true");
        session.setAttribute (CURRENTUSER, userName);
        session.setAttribute (USERID, userId);
        session.setAttribute (LASTACCESS, date);
    }

    /**
     * 设置退出
     */
    public void setLogOutStatus (HttpSession session) {
        session.setAttribute (LOGIN, "false");
    }

    /**
     * 取出登录名
     */
    public String getUserName (HttpSession session) {
        try {
            if (session.getAttribute (LOGIN).equals ("true")) {
                return session.getAttribute (CURRENTUSER).toString ();
            }
        } catch (NullPointerException e) {

        }
        return null;
    }

    /**
     * 取出登录名
     */
    public String getUserID (HttpSession session) {
        try {
            if (session.getAttribute (LOGIN).equals ("true")) {
                return session.getAttribute (USERID).toString ();
            }
        } catch (NullPointerException e) {

        }
        return null;
    }

    /**
     * @param contentType  输出指定文件的数据流(即文件下载或图片展示) contentType
     *                     文件类型，如果为空用application/octet-stream
     * @param fileName     文件名称，如果为空则使用系统随机命名，后缀为".file"
     * @param dispostition 文件由浏览器打开还是操作系统打开，即"inline" "attachment"
     * @param content      文件内容，如果为空，则不作处理
     * @param response     客户端响应
     */
    protected void download (String contentType, String fileName,
                             String dispostition, byte[] content, HttpServletResponse response, int cacheTime)
            throws IOException {
        ServletResponse res = null;
        while (response instanceof HttpServletRequestWrapper) {
            res = ((HttpServletResponseWrapper) response).getResponse ();
            if (res instanceof HttpServletResponse) {
                response = (HttpServletResponse) res;
            }
        }
        if (content != null && content.length > 0) {
            fileName = StringUtils.defaultIfEmpty (fileName,
                    "temp_" + String.valueOf (System.nanoTime ()) + ".file");
            contentType = StringUtils.defaultIfEmpty (contentType,
                    DEFALULT_CONTENT_TYPE);
            if (!INLINE.equals (dispostition)
                    && !ATTACHMENT.equals (dispostition)) {
                dispostition = ATTACHMENT;
            }
            ServletOutputStream out = null;
            try {
                response.setContentType (contentType);
                response.setHeader ("Content-Disposition", dispostition
                        + ";filename='"
                        + new String (fileName.getBytes ("GBK"), "ISO-8859-1")
                        + "'");
                if (cacheTime != -1) {
                    response.addHeader ("Cache-control:max-age", "" + cacheTime);
                }
                out = response.getOutputStream ();
                out.write (content, 0, content.length);
                out.flush ();
            } catch (Exception e) {

            } finally {
                if (out != null) {
                    out.close ();
                }
            }
        }
    }

    public void downloadFile (String contentType, String fileName,
                              byte[] content, HttpServletResponse response) throws IOException {
        download (contentType, fileName, ATTACHMENT, content, response, -1);
    }

    public void showPicture (String contentType, String fileName,
                             byte[] content, HttpServletResponse response, boolean cached) throws IOException {
        int cache = -1;
        if (cached) {
            cache = 36000;
        }
        download ("image/jpg", fileName, INLINE, content, response, cache);
    }

    public Date parseDate (String s) {
        if (s == null)
            return new Date ();
        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
        Date toDate;
        try {
            toDate = dateFormat.parse (s);
        } catch (ParseException e) {
            toDate = new Date ();
            e.printStackTrace ();
        }
        return toDate;

    }

    /**
     * 拼接展示内容
     */
    protected String getContent () {
        StringBuilder sb = new StringBuilder ();
        sb.append ("<font color='blue'><center>上传图片中</center></font><br/>");
        sb.append ("<center><span><img src='/resource/ajax-loader.gif' title='上传图片中' alt='上传图片中'></span></center>");
        return sb.toString ();
    }

    protected static String format (Date date) {

        return df.format (date);
    }

    public String replce (String where) {
        if (where == null) {
            return "";
        }
        where = where.replace ("*", "%");
        where = where.replace ("?", "_");
        return where;
    }

}
