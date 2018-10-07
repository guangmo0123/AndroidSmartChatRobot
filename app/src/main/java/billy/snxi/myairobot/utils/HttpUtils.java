package billy.snxi.myairobot.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 访问网络请求并获取返回值
 */
public class HttpUtils {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String API_ADDRESS = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "2731105e79244989a58a473570c7ffae";
    private static final String API_PW = "5f663a57beae7b6c";


    public static String getReplyMsgByServer(String msg) {
        String netURL = getURL(msg);
        Log.d("billy", netURL);
        try {
            URL url = new URL(netURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return dealResponseResult(conn.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getURL(String msg) {
        try {
            String url = API_ADDRESS + "?key=" + API_KEY + "&info=" + URLEncoder.encode(msg, CHARSET_NAME);
            return url;
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    /*
     * 处理服务器的响应结果（将输入流转化成字符串） Param : inputStream服务器的响应输入流
     */
    private static String dealResponseResult(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
            return new String(byteArrayOutputStream.toByteArray(), CHARSET_NAME);
        } catch (Exception e) {

        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }
}
