package Spider;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @ClassName:BiliSpider
 * @Auther: swmmmm
 * @Date: 2019/9/21 14:38
 * @Description:
 */
public class BiliSpider implements Runnable{
    private String url;
    private Integer page;
    public BiliSpider(){

    }
    public BiliSpider(String url,Integer page){
        this.url = url;
        this.page = page;
    }

    @Override
    public void run() {
        URL realUrl;
        HttpURLConnection connection;
        StringBuffer stringBuffer = new StringBuffer(url);
        if (page == 146){
            System.out.println("开始爬取番剧类型数据");
        }
        for (int i=page;i>145;i--){
            stringBuffer.append(i);
            try {
                realUrl = new URL(stringBuffer.toString());
                connection = (HttpURLConnection) realUrl.openConnection();
                connection.connect();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer stringBuffer1 = new StringBuffer();
                String str;
                while ( (str = bufferedReader.readLine()) != null) {
                    stringBuffer1.append(str);
                    System.out.println(str);
                }
                System.out.println(getMediaId(stringBuffer1.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringBuffer.delete(87,stringBuffer.length());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
    public static String[] getMediaId(String string){

        return null;
    }
}
