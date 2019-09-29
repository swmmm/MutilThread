package Main;

import Spider.BiliSpider;
import common.MybatisUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
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
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> aimUrls = new HashMap<>();
        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=1&pagesize=20&type=1&page=", 146);
        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=2&pagesize=20&type=1&page=", 69);
        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=3&pagesize=20&type=1&page=", 146);
        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=4&pagesize=20&type=1&page=", 29);
        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=5&pagesize=20&type=1&page=", 124);

        for (Map.Entry<String, Integer> entry : aimUrls.entrySet()) {
            BiliSpider biliSpider = new BiliSpider(entry.getKey(), entry.getValue());
            Thread thread = new Thread(biliSpider);
            thread.start();
            synchronized (threadCount) {
                threadCount++;
            }
        }
        while (threadCount != 0) {
            System.out.println("subthread hasn't finish");
            Thread.sleep(1000);
        }
        MybatisUtil.sqlSession.commit();
        MybatisUtil.sqlSession.close();
    }
}