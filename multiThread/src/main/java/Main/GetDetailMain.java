package Main;

import common.HttpUtil;
import common.JsonUtil;
import common.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: GetDetailMain
 * @description:
 * @author: swm
 * @create: 2019-09-19 14:47
 **/
public class GetDetailMain {
    public static Integer threadCount;

    public static void main(String[] args) {
        String url = "https://api.bilibili.com/pgc/view/web/media?media_id=";
        SqlSession sqlSession = MybatisUtil.sqlSession;

        List<String> ids = sqlSession.selectList("getMediaIds");
        if ((ids.size() % 300) != 0){
            threadCount = (ids.size() / 300) + 1;
        }else {
            threadCount = ids.size() / 300;
        }
        for (int i = 0; i < threadCount; i++) {
            int fromIndex = i * 300;
            int toIndex = fromIndex + 300;
            if (toIndex > ids.size()) {
                toIndex = ids.size();
            }
            List<String> subList = ids.subList(fromIndex, toIndex);

            new Thread(() -> {
                List<Map<String,Object>> data = new ArrayList<>();
                StringBuffer urlBuffer = new StringBuffer(url);
                //traversing the idArray,connect to the resource and add to the dataList
                System.out.println("启动一个线程开始爬取任务");
                for(String mediaId : subList){
                    urlBuffer.append(mediaId);

                    try {
                        data.add(JsonUtil.getVideoDetail(HttpUtil.getJsonString(urlBuffer.toString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    urlBuffer.delete(53,urlBuffer.length());
                }
                synchronized (threadCount){
                    System.out.println("一个爬取任务完成，开始插入");
                    int count = sqlSession.insert("insertBatchDetail",data);
                    System.out.println("插入"+count+"条数据");
                    threadCount--;
                }
                }).start();
        }
        while (threadCount!=0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sqlSession.commit();
        sqlSession.close();
        System.out.println("爬取结束");
    }
}
