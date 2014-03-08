package org.liyou.qixiaobo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 14-3-3.
 */
public class HttpUtil {
    /**
     * 发起http get请求获取网页源代码
     *
     * @param requestUrl
     * @return
     */
    public static String httpRequest (String requestUrl) {
        StringBuffer buffer = null;

        try {
            // 建立连接
            URL url = new URL (requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection ();
            httpUrlConn.setDoInput (true);
            httpUrlConn.setRequestMethod ("GET");

            // 获取输入流
            InputStream inputStream = httpUrlConn.getInputStream ();
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader (inputStreamReader);

            // 读取返回结果
            buffer = new StringBuffer ();
            String str = null;
            while ((str = bufferedReader.readLine ()) != null) {
                buffer.append (str);
            }

            // 释放资源
            bufferedReader.close ();
            inputStreamReader.close ();
            inputStream.close ();
            httpUrlConn.disconnect ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return buffer.toString ();
    }
}
