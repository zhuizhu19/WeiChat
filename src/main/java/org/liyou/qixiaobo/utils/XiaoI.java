package org.liyou.qixiaobo.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.liyou.qixiaobo.common.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 14-4-22.
 */
public class XiaoI implements IChat {
    private final static String app_key = "5onLhqNY9jnO";
    private final static String app_secret = "ah171hxKgC6tHem3fVUa";
    private static String sign;
    private static String nonce;
    private final static String host = "http://nlp.xiaoi.com";
    private final static String ask = "/robot/ask.do";
    private final static String recog = "/recog.do";
    private final static String synth = "/synth.do";
    public final static int ASK = 0;
    public final static int RECOG = 1;
    public final static int SYNTH = 2;
    private static XiaoI xiaoI = new XiaoI();

    static {
        sign();
    }

    private XiaoI () {

    }

    public static XiaoI getInstance () {
        return xiaoI;
    }

    @Override
    public String chat (String fromUserId, String chatContent, int type, Map<String, Object> map) {
        String postUrl = host;
        switch (type) {
            case ASK:
                postUrl += ask;
                break;
            case RECOG:
                postUrl += recog;
                break;
            case SYNTH:
                postUrl += synth;
                break;
            default:
                postUrl += ask;
                break;
        }
        String str = null;

        HttpClient hc = new HttpClient();
        PostMethod pm = new PostMethod(postUrl);
        pm.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                "utf-8");
        pm.addRequestHeader("X-Auth", "app_key=\"" + app_key + "\", nonce=\""
                + nonce + "\", signature=\"" + sign + "\"");
        pm.setParameter("platform", "weixin");
        pm.setParameter("type", "0");
        pm.setParameter("userId", fromUserId);
        pm.setParameter("question", chatContent);
        int re_code;
        try {
            re_code = hc.executeMethod(pm);
            if (re_code == 200) {
                str = pm.getResponseBodyAsString();
                System.out.print(str);
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;

    }

    private static void sign () {
        String realm = "xiaoi.com";
        String method = "POST";
        String uri = "/robot/ask.do";
        byte[] b = new byte[20];
        new Random().nextBytes(b);
        nonce = new String(Hex.encodeHex(b));
        String ha1 = DigestUtils.shaHex(StringUtils.join(new String[]{app_key, realm, app_secret}, ":"));
        String ha2 = DigestUtils.shaHex(StringUtils.join(new String[]{method, uri}, ":"));
        sign = DigestUtils.shaHex(StringUtils.join(new String[]{ha1, nonce, ha2}, ":"));
    }

    public static void main (String[] args) {
        XiaoI.getInstance().chat("123123", "我喜欢李尤!!!", XiaoI.ASK, null);
    }
}
