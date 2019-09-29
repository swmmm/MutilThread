package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ClassName: HttpUtil
 * @description:
 * @author: swm
 * @create: 2019-09-24 16:24
 **/
public class HttpUtil {
    public static String getJsonString(String url) throws IOException {
        URL realUrl = new URL(url);
        URLConnection connection = realUrl.openConnection();
        BufferedReader input =new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return input.readLine();
    }

}
