package Main;

import Spider.BiliSpider;

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
    public static void main(String[] args) {
        Map<String,Integer> aimUrls = new HashMap<>();
        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=1&pagesize=20&type=1&page=",146);
//        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=2&pagesize=20&type=1&page=",69);
//        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=3&pagesize=20&type=1&page=",145);
//        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=4&pagesize=20&type=1&page=",29);
//        aimUrls.put("https://api.bilibili.com/pgc/season/index/result?season_type=5&pagesize=20&type=1&page=",124);
        BiliSpider biliSpider = new BiliSpider();
        for(Map.Entry<String,Integer> entry : aimUrls.entrySet()){
            biliSpider.setUrl(entry.getKey());
            biliSpider.setPage(entry.getValue());
            System.out.println(biliSpider.getUrl()+biliSpider.getPage());
        }
        Thread thread = new Thread(biliSpider);
        thread.start();
    }
}