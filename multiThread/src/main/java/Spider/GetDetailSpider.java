package Spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: GetDetailSpider
 * @description:
 * @author: swm
 * @create: 2019-09-19 14:47
 **/
public class GetDetailSpider implements Runnable{
    private List<String> urls;

    @Override
    public void run() {
        for (String url : urls){
            try {
                Document document = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
