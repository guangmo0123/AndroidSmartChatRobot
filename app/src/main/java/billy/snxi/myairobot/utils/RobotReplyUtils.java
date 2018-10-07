package billy.snxi.myairobot.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import billy.snxi.myairobot.bean.ChatMsgBean;

/**
 * 图灵机器人回复专用处理类<br/>
 * 列出回复结果编码所对应的含义并进行结果处理
 */
public class RobotReplyUtils {
    /**
     * 100000	文本类
     * 200000	链接类
     * 302000	新闻类
     * 308000	菜谱类
     * 313000	儿歌类
     * 314000	诗词类
     */
    /**
     * 文本类
     */
    public static final int TYPE_CODE_TEXT = 100000;
    /**
     * 链接类
     */
    public static final int TYPE_CODE_LINK = 200000;
    /**
     * 新闻类
     */
    public static final int TYPE_CODE_NEWS = 302000;
    /**
     * 菜谱类
     */
    public static final int TYPE_CODE_FOOD_MENU = 308000;
    /**
     * 儿歌类
     */
    public static final int TYPE_CODE_CHILD_SONG = 313000;
    /**
     * 诗词类
     */
    public static final int TYPE_CODE_POEM_WORD = 314000;

    public static final String[] TYPE_CODE_NEWS_FIELDS = {"source", "article", "detailurl"};
    public static final String[] TYPE_CODE_FOOD_MENU_FIELDS = {"name", "info", "detailurl"};


    public static void addRealResultToList(List<ChatMsgBean> list, JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        int code = jsonObject.optInt("code");
        String result = jsonObject.optString("text");
        switch (code) {
            case TYPE_CODE_TEXT:
            case TYPE_CODE_CHILD_SONG:
            case TYPE_CODE_POEM_WORD:
                list.add(new ChatMsgBean(DateUtils.fromat(new Date()), result, ChatMsgBean.MsgType.MSG_FROM));
                break;
            case TYPE_CODE_LINK:
                result += "\r\n【URL】\r\n" + jsonObject.optString("url");
                list.add(new ChatMsgBean(DateUtils.fromat(new Date()), result, ChatMsgBean.MsgType.MSG_FROM));
                break;
            case TYPE_CODE_NEWS:
            case TYPE_CODE_FOOD_MENU:
                addListResultToList(list, jsonObject, code);
                break;
        }
    }

    private static void addListResultToList(List<ChatMsgBean> list, JSONObject jsonObject, int type) {
        JSONArray jsonArray = jsonObject.optJSONArray("list");
        int len = jsonArray.length();
        if (len == 0) {
            return;
        }
        String[] fields = null;
        if (type == TYPE_CODE_NEWS) {
            fields = TYPE_CODE_NEWS_FIELDS;
        } else if (type == TYPE_CODE_FOOD_MENU) {
            fields = TYPE_CODE_FOOD_MENU_FIELDS;
        } else {
            fields = new String[]{"detailurl"};
        }
        StringBuilder builder = new StringBuilder();
        int fieldCount = fields.length;
        JSONObject jsonNews;
        String field;
        for (int i = 0; i < len; i++) {
            jsonNews = jsonArray.optJSONObject(i);
            //清空字符串
            builder.setLength(0);
            builder.append(jsonObject.optString("text"));
            for (int j = 0; j < fieldCount; j++) {
                field = fields[j];
                if (!field.contains("url")) {
                    builder.append("\r\n【" + jsonNews.optString(field) + "】");
                } else {
                    builder.append("\r\n【URL】");
                    builder.append("\r\n" + jsonNews.optString(field) + "");
                }
            }
            list.add(new ChatMsgBean(DateUtils.fromat(new Date()), builder.toString(), ChatMsgBean.MsgType.MSG_FROM));
        }
    }

}
