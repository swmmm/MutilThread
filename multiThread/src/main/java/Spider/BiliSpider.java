package Spider;

import Main.BiliSpiderMain;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.MybatisUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:BiliSpider
 * @Auther: swmmmm
 * @Date: 2019/9/21 14:38
 * @Description:
 */
public class BiliSpider implements Runnable {
    private String url;
    private Integer page;
    private List<String> mediaList;

    public BiliSpider() {

    }

    public BiliSpider(String url, Integer page) {
        this.url = url;
        this.page = page;
        this.mediaList = new ArrayList<>();
    }

    @Override
    public void run() {
        URL realUrl;
        HttpURLConnection connection;
        StringBuffer urlBuffer = new StringBuffer(url);
        switch (page) {
            case 146:
                System.out.println("begin gather cartoon mediaId list");
                break;
            case 69:
                System.out.println("begin gather movie mediaId list");
                break;
            case 145:
                System.out.println("begin gather documentaty mediaId list");
                break;
            case 29:
                System.out.println("begin gather demestic mediaId list");
                break;
            case 124:
                System.out.println("begin gather TV drama mediaId list");
                break;
            default:
                System.out.println("There is no matching page ");
        }
        for (int i = page; i > 0; i--) {
            urlBuffer.append(i);
            try {
                realUrl = new URL(urlBuffer.toString());
                connection = (HttpURLConnection) realUrl.openConnection();
                connection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer resources = new StringBuffer();
                resources.append(bufferedReader.readLine());
                addToList(resources.toString());
                resources.setLength(0);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlBuffer.delete(87, urlBuffer.length());
        }
        synchronized (BiliSpiderMain.threadCount) {
            int i = MybatisUtil.sqlSession.insert("insertBatchMediaId", mediaList);
            System.out.println("Finish collection,a total of " + i + "mediaId were obtained,mediaListSIZE" + mediaList.size());
            BiliSpiderMain.threadCount--;
        }
    }


    public void addToList(String response) {
        JSONArray data = JSONObject.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (Object object : data) {
            JSONObject info = (JSONObject) object;
            mediaList.add(info.get("media_id").toString());
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
}
