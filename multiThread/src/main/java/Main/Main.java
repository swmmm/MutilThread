package Main;

import Spider.HtmlSpider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName:Main
 * @Auther: swmmmm
 * @Date: 2019/9/15 11:13
 * @Description:
 */
public class Main {
    public volatile static Set<String> urls = new HashSet<>();
    public volatile static  int threadCount = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean unfinish = true;
        int lastIndex = 1;//1191;
        int beginPage = 1;
        int endPage = beginPage+4;
        while (unfinish){
            if (endPage >=lastIndex){
                endPage = lastIndex;
                unfinish = false;
            }
            Runnable spider = new HtmlSpider(beginPage,endPage);
            Thread thread = new Thread(spider);
            threadCount +=1;
            thread.start();
            System.out.println("线程"+thread.getName()+"启动");
            beginPage = endPage+1;
            endPage = beginPage+4;
        }
        while (true){
            if (threadCount == 0){
                System.out.println(urls.size()/36);
                break;
            }
            System.out.println("子线程未完成--------");
            Thread.sleep(2000);
        }


    }
}
