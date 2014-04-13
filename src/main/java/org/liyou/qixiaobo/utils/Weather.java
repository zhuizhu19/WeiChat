package org.liyou.qixiaobo.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-4-13.
 */
public class Weather {
    /**
     * "error":0,
     * "status":"success",
     * "date":"2014-04-13",
     * "results":[
     * {
     * "currentCity":"\u5fb7\u60e0",
     * "weather_data":[
     * {
     * "date":"\u5468\u65e5(\u4eca\u5929, \u5b9e\u65f6\uff1a20\u2103)",
     * "dayPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/day\/qing.png",
     * "nightPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/night\/qing.png",
     * "weather":"\u6674",
     * "wind":"\u897f\u5357\u98ce3-4\u7ea7",
     * "temperature":"6\u2103"
     * },
     * {
     * "date":"\u5468\u4e00",
     * "dayPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/day\/duoyun.png",
     * "nightPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/night\/duoyun.png",
     * "weather":"\u591a\u4e91",
     * "wind":"\u897f\u5357\u98ce5-6\u7ea7",
     * "temperature":"24 ~ 5\u2103"
     * },
     * {
     * "date":"\u5468\u4e8c",
     * "dayPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/day\/qing.png",
     * "nightPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/night\/duoyun.png",
     * "weather":"\u6674\u8f6c\u591a\u4e91",
     * "wind":"\u897f\u5317\u98ce3-4\u7ea7",
     * "temperature":"16 ~ 2\u2103"
     * },
     * {
     * "date":"\u5468\u4e09",
     * "dayPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/day\/duoyun.png",
     * "nightPictureUrl":"http:\/\/api.map.baidu.com\/images\/weather\/night\/xiaoyu.png",
     * "weather":"\u591a\u4e91\u8f6c\u5c0f\u96e8",
     * "wind":"\u897f\u5357\u98ce\u5fae\u98ce",
     * "temperature":"16 ~ 5\u2103"
     * }
     * ]
     * }
     * ]
     */
    private final static String page_url = "http://api.map.baidu.com/telematics/v3/weather?location=%location%&output=json&ak=B840be83881db76cd00aeead221c7e0a";
    String city;
    String date;
    public String status;
    List<WeatherInfo> weatherInfos;

    public String getCity () {
        return city;
    }

    public void setCity (String city) {
        this.city = city;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public List<WeatherInfo> getWeatherInfos () {
        return weatherInfos;
    }

    public void setWeatherInfos (List<WeatherInfo> weatherInfos) {
        this.weatherInfos = weatherInfos;
    }

    public Weather (String city) throws IOException, NullPointerException {
        this.city = city;
        city = java.net.URLEncoder.encode (city, "utf-8");
        final String new_page_url = page_url.replace ("%location%", city);
        URL url = new URL (new_page_url);
        BufferedReader br;
        StringBuilder sb = null;
        URLConnection connectionData = url.openConnection ();
        connectionData.setConnectTimeout (1000);
        try {
            br = new BufferedReader (new InputStreamReader (
                    connectionData.getInputStream (), "UTF-8"));
            sb = new StringBuilder ();
            String line = null;
            while ((line = br.readLine ()) != null)
                sb.append (line);
        } catch (SocketTimeoutException e) {
            System.out.println ("连接超时");
        } catch (FileNotFoundException e) {
            System.out.println ("加载文件出错");
        }
        String datas = sb.toString ();
        System.out.println (datas);
        JSONObject jsonData = JSONObject.fromObject (datas);
        //  System.out.println(jsonData.toString());
        String error = jsonData.getString ("error");
        status = jsonData.getString ("status");
        if (error.equals ("0")) {
            String date = jsonData.getString ("date");
            this.date = date;
            JSONArray results = jsonData.getJSONArray ("results");
            if (results.isEmpty ()) {
                return;
            }
            JSONObject result = results.getJSONObject (0);
            this.city = result.getString ("currentCity");
            JSONArray weather = result.getJSONArray ("weather_data");
            weatherInfos = new ArrayList<WeatherInfo> (3);
            for (int i = 0; i < 3; i++) {
                JSONObject jsonObject = weather.getJSONObject (i);
                WeatherInfo info = new WeatherInfo ();
                info.setDate (jsonObject.getString ("date"));
                info.setDayPictureUrl (jsonObject.getString ("dayPictureUrl"));
                info.setNightPictureUrl (jsonObject.getString ("nightPictureUrl"));
                info.setWeather (jsonObject.getString ("weather"));
                info.setTemperature (jsonObject.getString ("temperature"));
                weatherInfos.add (info);
            }

        }

    }



    /*  int Ctiyid;
    URLConnection connectionData;
    StringBuilder sb;
    BufferedReader br;// 读取data数据流
    JSONObject jsonData;
    JSONObject info;

    //从天气网解析的参数
    String city;// 城市
    String date_y;//日期
    String week;// 星期
    String fchh;// 发布时间

    String weather1;// 未来1到6天天气情况
    String weather2;
    String weather3;
    String weather4;
    String weather5;
    String weather6;

    String wind1;//未来1到6天风况
    String wind2;
    String wind3;
    String wind4;
    String wind5;
    String wind6;

    String fl1;//风的等级
    String fl2;
    String fl3;
    String fl4;
    String fl5;
    String fl6;


    String temp1;// 未来1到6天的气温
    String temp2;
    String temp3;
    String temp4;
    String temp5;
    String temp6;

    String index;// 今天的穿衣指数
    String index_uv;// 紫外指数
    String index_tr;// 旅游指数
    String index_co;// 舒适指数
    String index_cl;// 晨练指数
    String index_xc;//洗车指数
    String index_d;//天气详细穿衣指数


    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder ();
        stringBuilder.append (city);
        stringBuilder.append ("  ");
        stringBuilder.append (date_y);
        stringBuilder.append ("\r\n");
        stringBuilder.append ("未来2天天天气情况\r\n");
        stringBuilder.append (weather1).append (" ");
        stringBuilder.append (weather2).append (" ");
        stringBuilder.append ("\r\n");
        stringBuilder.append ("未来2天风况\r\n");
        stringBuilder.append (wind1).append (" ");
        stringBuilder.append (wind2).append (" ");
        stringBuilder.append ("\r\n");
        stringBuilder.append ("未来2天的气温\r\n");
        stringBuilder.append (temp1).append (" ");
        stringBuilder.append (temp2).append (" ");
        stringBuilder.append ("\r\n");
        stringBuilder.append ("今天的穿衣指数\r\n");
        stringBuilder.append (index);
        stringBuilder.append ("\r\n");
        stringBuilder.append ("今天的穿衣指数\r\n");
        stringBuilder.append (index);
        stringBuilder.append ("\r\n");
        stringBuilder.append ("今天的紫外指数\r\n");
        stringBuilder.append (index_uv);
        stringBuilder.append ("\r\n");
        stringBuilder.append ("今天的旅游指数\r\n");
        stringBuilder.append (index_tr);
        stringBuilder.append ("\r\n");
        stringBuilder.append ("今天的旅游指数\r\n");
        stringBuilder.append (index_tr);
        stringBuilder.append ("\r\n");
        stringBuilder.append ("今天的舒适指数\r\n");
        stringBuilder.append (index_co);
        stringBuilder.append ("\r\n");
        stringBuilder.append ("天气详细穿衣指数\r\n");
        stringBuilder.append (index_d);
        stringBuilder.append ("\r\n");
        return stringBuilder.toString ();
    }

    public Weather (int Cityid) throws IOException, NullPointerException {
        // 解析本机ip地址

        this.Ctiyid = Cityid;
        // 连接中央气象台的API
        URL url = new URL ("http://m.weather.com.cn/data/" + Ctiyid + ".html");
        connectionData = url.openConnection ();
        connectionData.setConnectTimeout (1000);
        try {
            br = new BufferedReader (new InputStreamReader (
                    connectionData.getInputStream (), "UTF-8"));
            sb = new StringBuilder ();
            String line = null;
            while ((line = br.readLine ()) != null)
                sb.append (line);
        } catch (SocketTimeoutException e) {
            System.out.println ("连接超时");
        } catch (FileNotFoundException e) {
            System.out.println ("加载文件出错");
        }
        String datas = sb.toString ();

        jsonData = JSONObject.fromObject (datas);
        //  System.out.println(jsonData.toString());
        info = jsonData.getJSONObject ("weatherinfo");

        city = info.getString ("city").toString ();
        System.out.println (city);
        week = info.getString ("week").toString ();
        date_y = info.getString ("date_y").toString ();
        fchh = info.getString ("fchh").toString ();
        //1到6天的天气
        weather1 = info.getString ("weather1").toString ();
        System.out.println (weather1);
        weather2 = info.getString ("weather2").toString ();
        weather3 = info.getString ("weather3").toString ();
        weather4 = info.getString ("weather4").toString ();
        weather5 = info.getString ("weather5").toString ();
        weather6 = info.getString ("weather6").toString ();
        //1到6天的气温
        temp1 = info.getString ("temp1").toString ();
        temp2 = info.getString ("temp2").toString ();
        temp3 = info.getString ("temp3").toString ();
        temp4 = info.getString ("temp4").toString ();
        temp5 = info.getString ("temp5").toString ();
        temp6 = info.getString ("temp6").toString ();
        //1到6天的风况
        wind1 = info.getString ("wind1").toString ();
        wind2 = info.getString ("wind2").toString ();
        wind3 = info.getString ("wind3").toString ();
        wind4 = info.getString ("wind4").toString ();
        wind5 = info.getString ("wind5").toString ();
        wind6 = info.getString ("wind6").toString ();
        //1到6天的风速
        fl1 = info.getString ("fl1").toString ();
        fl2 = info.getString ("fl2").toString ();
        fl3 = info.getString ("fl3").toString ();
        fl4 = info.getString ("fl4").toString ();
        fl5 = info.getString ("fl5").toString ();
        fl6 = info.getString ("fl6").toString ();
        //各种天气指数
        index = info.getString ("index").toString ();
        index_uv = info.getString ("index_uv").toString ();
        index_tr = info.getString ("index_tr").toString ();
        index_co = info.getString ("index_co").toString ();
        index_cl = info.getString ("index_cl").toString ();
        index_xc = info.getString ("index_xc").toString ();
        index_d = info.getString ("index_d").toString ();

    }

    public static void main (String[] args) {
        try {
            new Weather (101270803); // 101270803就是你的城市代码
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
    }*/

    public class WeatherInfo {
        String date;
        String dayPictureUrl;
        String nightPictureUrl;
        String weather;
        String temperature;

        public String getDate () {
            return date;
        }

        public void setDate (String date) {
            this.date = date;
        }

        public String getDayPictureUrl () {
            return dayPictureUrl;
        }

        public void setDayPictureUrl (String dayPictureUrl) {
            this.dayPictureUrl = dayPictureUrl;
        }

        public String getNightPictureUrl () {
            return nightPictureUrl;
        }

        public void setNightPictureUrl (String nightPictureUrl) {
            this.nightPictureUrl = nightPictureUrl;
        }

        public String getWeather () {
            return weather;
        }

        public void setWeather (String weather) {
            this.weather = weather;
        }

        public String getTemperature () {
            return temperature;
        }

        public void setTemperature (String temperature) {
            this.temperature = temperature;
        }

        WeatherInfo (String date, String dayPictureUrl, String nightPictureUrl, String weather, String temperature) {
            this.date = date;
            this.dayPictureUrl = dayPictureUrl;
            this.nightPictureUrl = nightPictureUrl;
            this.weather = weather;
            this.temperature = temperature;
        }

        WeatherInfo () {

        }
    }
}
