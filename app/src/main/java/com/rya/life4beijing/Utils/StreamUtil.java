package com.rya.life4beijing.Utils;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;

import static android.content.ContentValues.TAG;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */
public class StreamUtil {

    public static String streamToString(InputStream fileInputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer stringBuffer = new StringBuffer();
        String buf = null;
        int len = 0;

        while ((buf = bufferedReader.readLine()) != null) {
            stringBuffer.append(buf);
        }
        return stringBuffer.toString();
    }



    public static void writeFileToCache(Activity mActivity, String data, String fileName) throws IOException {
        File file = new File(mActivity.getFilesDir().getPath() , fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] bytes = data.getBytes();

        fileOutputStream.write(bytes);

        System.out.println("数据 >>>>>>>>>> 写入成功！~");

        fileOutputStream.close();
    }

}
