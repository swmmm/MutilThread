package Main;

import Spider.HtmlSpider;
import common.MybatisUtil;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName:Main
 * @Auther: swmmmm
 * @Date: 2019/9/15 11:13
 * @Description:
 */
public class Main {
    public static Integer threadCount = 0;
    public static SqlSession sqlSession = MybatisUtil.sqlSession;

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean unfinish = true;
        int lastIndex = 1194;
        int beginPage = 1;
        int endPage = beginPage + 25;
        while (unfinish) {
            if (unfinish && (endPage > lastIndex)) {
                endPage = lastIndex;
                unfinish = false;
            }
            Runnable spider = new HtmlSpider(beginPage, endPage);
            Thread thread = new Thread(spider);
            synchronized (threadCount) {
                threadCount += 1;
                System.out.println("主线程操作threadCount=" + threadCount);
            }
            thread.start();
            System.out.println("线程" + thread.getName() + "启动");
            beginPage = endPage + 1;
            endPage = beginPage + 25;
        }
        while (true) {
            if (threadCount == 0) {
                sqlSession.commit();
                System.out.println("事物提交");
                sqlSession.close();
                break;
            }
            System.out.println("子线程未完成--------");
            Thread.sleep(2000);
        }


    }
}
