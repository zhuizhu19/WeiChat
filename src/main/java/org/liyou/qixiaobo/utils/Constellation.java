package org.liyou.qixiaobo.utils;

/**
 * Created by Administrator on 14-4-13.
 */

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * http://api.uihoo.com/astro/astro.http.php?fun=love&id=星座ID&format=数据格式
 * URL参数说明
 * ===============================================
 *
 * @param string  fun         函数类型(day,tomorrow,week,month,year,love)
 * @param integer id          星座编号(必填)
 * @param string  format      数据格式(json,jsonp,xml)
 * @param string  callback    只有当数据格式为jsonp时,callback参数才有效
 *                =========================================
 */

public class Constellation {
    /**
     * <option value="0" selected="selected">白羊座(03/21-04/19) [id参数:0]</option>
     * <option value="1">金牛座(04/20-05/20) [id参数:1]</option>
     * <option value="2">双子座(05/21-06/21) [id参数:2]</option>
     * <option value="3">巨蟹座(06/22-07/22) [id参数:3]</option>
     * <option value="4">狮子座(07/23-08/22) [id参数:4]</option>
     * <option value="5">处女座(08/23-09/22) [id参数:5]</option>
     * <option value="6">天秤座(09/23-10/23) [id参数:6]</option>
     * <option value="7">天蝎座(10/24-11/22) [id参数:7]</option>
     * <option value="8">射手座(11/23-12/21) [id参数:8]</option>
     * <option value="9">魔羯座(12/22-01/19) [id参数:9]</option>
     * <option value="10">水瓶座(01/20-02/18) [id参数:10]</option>
     * <option value="11">双鱼座(02/19-03/20) [id参数:11]</option>
     */
    private int value;
    private List<Item> items;
    private String date;
    private String constellation;
    private static final String page_url = " http://api.uihoo.com/astro/astro.http.php?fun=%fun%&id=%id%&format=json";

    public int getValue () {
        return value;
    }

    public void setValue (int value) {
        this.value = value;
    }

    public List<Item> getItems () {
        return items;
    }

    public void setItems (List<Item> items) {
        this.items = items;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getConstellation () {
        return constellation;
    }

    public void setConstellation (String constellation) {
        this.constellation = constellation;
    }

    public static String getPage_url () {
        return page_url;
    }

    public Constellation (int value) throws IOException {
        final String new_page_url = page_url.replace("%fun%", "day").replace("%id%", String.valueOf(value));
        String datas = Weather.getConnectContent(new_page_url);
        JSONArray array = JSONArray.fromObject(datas);
        items = new ArrayList<Item>(10);
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            JSONObject jsonObject = array.getJSONObject(i);
            item.setTitle(jsonObject.getString("title"));
            item.setRank(Integer.parseInt(jsonObject.getString("rank")));
            item.setValue(jsonObject.getString("value"));
            items.add(item);
        }
        constellation = array.getJSONObject(10).getString("cn");
        date = array.getString(11).toString();
    }

    public static String getConstellationString () {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("xz0 :白羊座(03/21-04/19)\n");
        stringBuilder.append("xz1 :金牛座(04/20-05/20)\n");
        stringBuilder.append("xz2 :双子座(05/21-06/21)\n");
        stringBuilder.append("xz3 :巨蟹座(06/22-07/22)\n");
        stringBuilder.append("xz4 :狮子座(07/23-08/22)\n");
        stringBuilder.append("xz5 :处女座(08/23-09/22)\n");
        stringBuilder.append("xz6 :天秤座(09/23-10/23)\n");
        stringBuilder.append("xz7 :天蝎座(10/24-11/22)\n");
        stringBuilder.append("xz8 :射手座(11/23-12/21)\n");
        stringBuilder.append("xz9 :魔羯座(12/22-01/19)\n");
        stringBuilder.append("xz10:水瓶座(01/20-02/18)\n");
        stringBuilder.append("xz11:双鱼座(02/19-03/20)\n");
        return stringBuilder.toString();
    }

    public class Item {
        private String title;
        private int rank;
        private String value;

        public String getTitle () {
            return title;
        }

        public void setTitle (String title) {
            this.title = title;
        }

        public int getRank () {
            return rank;
        }

        public void setRank (int rank) {
            this.rank = rank;
        }

        public String getValue () {
            return value;
        }

        public void setValue (String value) {
            this.value = value;
        }

        public String getRankString (int rank) {
            String str;
            switch (rank) {
                case 0:
                    str = " \ue421";
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    str = " ";
                    for (int i = 0; i < rank; i++) {
                        str += "\ue00e ";
                    }
                default:
                    str = "";
                    for (int i = 0; i < 5; i++) {
                        str += "\ue00e ";
                    }
                    break;

            }
            return str;
        }
    }

    public static void main (String[] args) {
        try {
            System.out.print(new Constellation(1).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
