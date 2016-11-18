package com.rya.life4beijing.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class HttpUtil {

    public static InputStream getDataFromOkHttp() {


        return null;
    }

    public static InputStream getData(String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            connection.setRequestMethod("GET");
            //设置请求超时
            connection.setConnectTimeout(1000 * 3);

            int code = connection.getResponseCode();
            if (code == 200) {
                return connection.getInputStream();
            } else {
                return null;
            }
        } catch (SocketTimeoutException sktExp) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
