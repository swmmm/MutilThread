package Spider;

import Main.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;



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
        while(beginPage<=endPage) {
            try {
                Document document = Jsoup.connect("http://www.meijuhui.net/lib/new-----"+beginPage+".html").get();
                Elements elements = document.select("a.list_img");
                for (Element element : elements){
//                    System.out.println(element.attr("href"));
                    Main.urls.add(element.attr("abs:href"));
                }
                beginPage++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Main.threadCount -=1;
        System.out.println("线程"+Thread.currentThread().getName()+"结束");
    }
}
