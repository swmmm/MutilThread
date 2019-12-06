package Main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName:BiliSpiderMain
 * @Auther: swmmmm
 * @Date: 2019/9/21 14:52
 * @Description:
 */
public class BiliSpiderMain {
    public static Integer threadCount = 25;
    private static ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    private static ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;
    private static SqlSession sqlSession = MybatisUtil.sqlSession;
    private static List<String> aimUrls = new ArrayList<>();

    static {
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
    }


    public static void main(String[] args) throws Exception {
        for (String tempUrl : aimUrls) {
            pool.execute(()->{
                StringBuffer response = new StringBuffer();
                JSONObject data;
                StringBuffer pageUrl=new StringBuffer(tempUrl);
                int page = 1;
                while (true) {
                    pageUrl.append(page);
                    String str = connectGetRes(pageUrl.toString());
                    pageUrl.delete(87, pageUrl.length());
                    if (str != null) {//连接成功
                        response.append(str);
//                  System.out.println(response.toString());
                        data = JSON.parseObject(response.toString()).getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");//数据list
                        response.setLength(0);//清空response
                        //每个线程处理一页20个mediaId数据
                        pool.execute(() -> {
                            StringBuffer mediaUrl = new StringBuffer("https://api.bilibili.com/pgc/view/web/media?media_id=");
                            StringBuffer videoInfo = new StringBuffer();
                            List<Map<String, Object>> infoList = new ArrayList<>();
                            List<Map<String,String>> videoStyleList = new ArrayList<>();
                            //循环获取当页meidiaInfo，
                            for (Object object : list) {
                                JSONObject jsonObject = (JSONObject) object;
                                mediaUrl.append(jsonObject.getString("media_id"));
                                String str1 = connectGetRes(mediaUrl.toString());
                                mediaUrl.delete(53, mediaUrl.length());
                                if (str1 != null) {
                                    videoInfo.append(str1);
                                    infoList.add(getVideoDetail(videoInfo.toString(),videoStyleList));
                                    videoInfo.setLength(0);
                                }
                            }
                            //插入一页数据
                            infoList.forEach(map-> {
                                if (map.containsKey("videoStyles")) {
                                    videoStyleList.addAll((Collection<? extends Map<String, String>>) map.get("videoStyles"));
                                }
                            });
                            synchronized (threadCount){
                                sqlSession.insert("videoResourcesMapper.insertVideos",infoList);
                                sqlSession.insert("videoResourcesMapper.insertVideoStyle",videoStyleList);
                                sqlSession.commit();
                            }
                        });

                        //判断是否有下一页,1:有 0：无   无则跳出当前循环
                        if (data.getIntValue("has_next") == 0) {
                            break;
                        }
                    }
                    if (page % 10 == 0) {
                        System.out.println("正在处理"+pageUrl+page);
                    }
                    page++;
                }
            });
        }


        while (pool.getActiveCount() != 0) {
            System.out.println("线程池中正在执行任务的线程数量" + pool.getActiveCount());
            System.out.println("线程池任务总数" + pool.getTaskCount());
            System.out.println("线程池已完成任务数量" + pool.getCompletedTaskCount());
            Thread.sleep(3000);
        }

        pool.shutdown();
    }

    public static String connectGetRes(String url) {
        String response = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(2000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = bufferedReader.readLine();
        } catch (SocketTimeoutException ste) {
            System.out.println(url.toString() + "连接超时");
            return null;
        } catch (IOException e) {
            System.out.println(url.toString() + "连接异常");
            return null;
        }
        return response;
    }

    public static Map<String, Object> getVideoDetail(String jsonString,List<Map<String,String>> videoStyleList) {
        Map<String, Object> map = new HashMap<>();
        JSONObject result = JSONObject.parseObject(jsonString).getJSONObject("result");
        JSONObject publish = result.getJSONObject("publish");
        JSONArray areas = result.getJSONArray("areas");
        JSONArray styles = result.getJSONArray("styles");
        //获取影片基本信息
        map.put("mediaId", result.getString("media_id"));
        map.put("name", result.getString("origin_name"));
        map.put("title", result.getString("title"));
        map.put("alias", result.getString("alias"));
        map.put("actors", result.getString("actors"));
        map.put("staff", result.getString("staff"));
        map.put("cover", result.getString("cover"));
        map.put("brief", result.getString("evaluate"));
        map.put("publishDate", publish.getString("pub_date"));
        map.put("status", publish.getString("time_length_show"));
        //影片关联信息
        if (areas != null && areas.size() > 0) {
            map.put("areaId", areas.getJSONObject(0).get("id"));
            map.put("areaName", areas.getJSONObject(0).get("name"));
        }
        map.put("typeId", result.getString("type"));
        map.put("typeName", result.get("type_name"));
        //videoStyles
//        List<Map<String, String>> stylesList = new ArrayList<>();
        if (styles != null && styles.size() > 0) {
            for (int i = 0; i < styles.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                JSONObject jsonObject = styles.getJSONObject(i);
                map1.put("mediaId", result.getString("media_id"));
                map1.put("styleId", jsonObject.getString("id"));
                map1.put("styleName", jsonObject.getString("name"));
                videoStyleList.add(map1);
            }
//            map.put("videoStyles", stylesList);
        }
        return map;
    }
}