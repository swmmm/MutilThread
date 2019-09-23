package Spider;

import Main.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName:HtmlSpider
 * @Auther: swmmmm
 * @Date: 2019/9/14 20:22
 * @Description:
 */
public class HtmlSpider implements Runnable{
    private int beginPage = 0;
    private int endPage = 0;

    public HtmlSpider(int beginPage,int endPage) {
        this.beginPage = beginPage;
        this.endPage = endPage;
    }

    @Override
    public void run() {
        List<String> url = new ArrayList<>();
        int first = beginPage;
        while(beginPage<=endPage) {
            Document document = null;
            try {
                document = Jsoup.connect("http://www.meijuhui.net/lib/new-----"+beginPage+".html").get();
            } catch (IOException e){
                System.out.println("发生异常，第"+beginPage+"页连接超时，爬取失败");
                continue;
            }
            Elements elements = document.select("a.list_img");
                for (Element element : elements){
//                    String string = element.attr("abs:href");
//                    url.add((String) string.subSequence(0,string.length()-5));
                    url.add(element.attr("abs:href"));
                }
                beginPage++;
        }
        synchronized (Main.threadCount){
            int size = Main.sqlSession.insert("videoResourcesMapper.insertBatch",url);
            System.out.println("爬取第"+first+"--"+endPage+"页完成，共"+size+"条数据");
            Main.threadCount -=1;
            System.out.println("线程"+Thread.currentThread().getName()+"结束\n操作threadcount="+ Main.threadCount);
        }

    }
}
