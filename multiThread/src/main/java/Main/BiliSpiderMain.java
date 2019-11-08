package Main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:BiliSpiderMain
 * @Auther: swmmmm
 * @Date: 2019/9/21 14:52
 * @Description:
 */
public class BiliSpiderMain {
    public static Integer threadCount = 0;
    private static List<String> mediaIds = new ArrayList<>();
    private static List<Map<String, Object>> data = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        List<String> aimUrls = new ArrayList<>();
        //cartoon mediaId list
        aimUrls.add("https://api.bilibili.com/pgc/season/index/result?season_type=1&pagesize=20&type=1&page=");
        //movie mediaId list
        aimUrls.add("https://api.bilibili.com/pgc/season/index/result?season_type=2&pagesize=20&type=1&page=");
        //documentaty mediaId list
        aimUrls.add("https://api.bilibili.com/pgc/season/index/result?season_type=3&pagesize=20&type=1&page=");
        //demestic mediaId list
        aimUrls.add("https://api.bilibili.com/pgc/season/index/result?season_type=4&pagesize=20&type=1&page=");
        //TV drama mediaId list
        aimUrls.add("https://api.bilibili.com/pgc/season/index/result?season_type=5&pagesize=20&type=1&page=");



        //获取全部mediaID
        for (String url : aimUrls) {
            new Thread(() -> {
                //启动一个新线程开始获取mediaId
                synchronized (threadCount) {
                    threadCount++;
                    System.out.println(Thread.currentThread().getName() + "启动");
                }
                List<String> tempMID = new ArrayList<>();
                StringBuffer resources = new StringBuffer();
                int page = 1;
                StringBuffer stringBuffer = new StringBuffer(url);
                JSONArray list;
                JSONObject data;
                for (; ; ) {
                    stringBuffer.append(page);
                    String response = connectGetRes(stringBuffer.toString());
                    data = JSONObject.parseObject(response).getJSONObject("data");
                    list = data.getJSONArray("list");
                    for (Object object : list) {
                        JSONObject info = (JSONObject) object;
                        tempMID.add(info.get("media_id").toString());
                    }
                    if (data.get("has_next").toString().equals("0")) {
                        stringBuffer.delete(87, stringBuffer.length());
                        resources.setLength(0);
                        page += 1;
                    } else {
                        synchronized (threadCount) {
                            System.out.println(Thread.currentThread().getName() + stringBuffer.toString() + "结束");
                            System.out.println("mediaID total：  " + tempMID.size());
                            mediaIds.addAll(tempMID);
                            threadCount--;
                        }
                        break;
                    }
                }
            }
            ).start();
        }
        while (true) {
            Thread.sleep(1000);
            if (threadCount == 0) {
                System.out.println(mediaIds.size());
                break;
            }
        }

        //获取详细信息
        int fromIndex = 0;
        int toIndex=200;
        boolean end =false;
        while (!end) {
            if (toIndex>=mediaIds.size()){
                toIndex=mediaIds.size();
                end = true;
            }
            //每200个mediaID开启一个线程爬取
            List<String> subMediaIds = mediaIds.subList(fromIndex,toIndex);
            new Thread(() -> {
                StringBuffer infoUrl = new StringBuffer("https://api.bilibili.com/pgc/view/web/media?media_id=");
                List<Map<String, Object>> tempData = new ArrayList<>();
                synchronized (threadCount) {
                    threadCount++;
                    System.out.println(Thread.currentThread().getName() + "启动"+subMediaIds.size());
                }
                for (String mediaId : subMediaIds) {
                    String response = connectGetRes(infoUrl.append(mediaId).toString());
                    System.out.println(response);
                    tempData.add(getVideoDetail(response));
                    infoUrl.delete(53, infoUrl.length());
                }
                //爬取结束
                synchronized (threadCount) {
                    data.addAll(tempData);
                    threadCount--;
                    System.out.println(Thread.currentThread().getName() + "结束"+tempData.size());
                }
            }).start();
            fromIndex=toIndex;
            toIndex+=10;
        }
    }

    public static String connectGetRes(String url) {
        String response = "";
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = bufferedReader.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Map<String, Object> getVideoDetail(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        JSONObject result = JSONObject.parseObject(jsonString).getJSONObject("result");
        JSONObject publish = result.getJSONObject("publish");
        JSONArray jsonAreas = result.getJSONArray("areas");

        Map<String, String> areas = new HashMap<>();
        for (int i = 0; i < jsonAreas.size(); i++) {
            JSONObject jsonObject = jsonAreas.getJSONObject(i);
            areas.put(result.getString("media_id"), jsonObject.getString("id"));
        }

        JSONArray jsonStyles = result.getJSONArray("styles");
        Map<String, String> styles = new HashMap<>();
        for (int i = 0; i < jsonStyles.size(); i++) {
            JSONObject jsonObject = jsonStyles.getJSONObject(i);
            styles.put(result.getString("media_id"), jsonObject.getString("id"));
        }
//        map.put("mediaId",Integer.valueOf(result.getString("media_id")));
        map.put("mediaId", result.getString("media_id"));
        map.put("title", result.getString("title"));
        map.put("actors", result.getString("actors"));
        map.put("alias", result.getString("alias"));
        map.put("cover", result.getString("cover"));
        map.put("evaluate", result.getString("evaluate"));
        map.put("originName", result.getString("origin_name"));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = sdf.parse(publish.getString("pub_date"));
//        map.put("publishDate",date);
        map.put("publishDate", publish.getString("pub_date"));
        map.put("status", publish.getString("time_length_show"));
        map.put("staff", result.getString("staff"));
        map.put("type", result.getString("type"));
        map.put("areas", areas);
        map.put("styles", styles);
        return map;
    }
}