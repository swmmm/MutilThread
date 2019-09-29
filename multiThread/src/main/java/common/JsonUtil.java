package common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.Video;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: JsonUtil
 * @description:
 * @author: swm
 * @create: 2019-09-24 08:46
 **/
public class JsonUtil {
    public static Map<String, Object> getVideoDetail(String jsonString) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        JSONObject result = JSONObject.parseObject(jsonString).getJSONObject("result");
        JSONObject publish = result.getJSONObject("publish");

        JSONArray jsonAreas = result.getJSONArray("areas");
        Map<String,String> areas = new HashMap<>();
        for (int i=0;i<jsonAreas.size();i++){
            JSONObject jsonObject = jsonAreas.getJSONObject(i);
            areas.put(result.getString("media_id"),jsonObject.getString("id"));
        }

        JSONArray jsonStyles = result.getJSONArray("styles");
        Map<String,String> styles = new HashMap<>();
        for (int i=0;i<jsonStyles.size();i++){
            JSONObject jsonObject = jsonStyles.getJSONObject(i);
            styles.put(result.getString("media_id"),jsonObject.getString("id"));
        }
//        map.put("mediaId",Integer.valueOf(result.getString("media_id")));
        map.put("mediaId",result.getString("media_id"));
        map.put("title", result.getString("title"));
        map.put("actors", result.getString("actors"));
        map.put("alias", result.getString("alias"));
        map.put("cover", result.getString("cover"));
        map.put("evaluate", result.getString("evaluate"));
        map.put("originName",result.getString("origin_name"));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = sdf.parse(publish.getString("pub_date"));
//        map.put("publishDate",date);
        map.put("publishDate",publish.getString("pub_date"));
        map.put("status", publish.getString("time_length_show"));
        map.put("staff", result.getString("staff"));
        map.put("type", result.getString("type"));
        map.put("areas",areas);
        map.put("styles",styles);
        return map;
    }
}
