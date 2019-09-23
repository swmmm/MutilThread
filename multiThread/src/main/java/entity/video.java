package entity;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: video
 * @description:
 * @author: swm
 * @create: 2019-09-23 08:54
 **/
public class video {
    private Integer id;
    private String title;
    private String actors;
    private String alias;
    private List<Area> areas;
    //封面
    private String cover;
    private String evaluate;
    private String originName;
    private Date publishDate;
    private String status;
    private String staff;
    private List<Style> styles;
    private Type type;


}
class Type{
    private Integer id;
    private String name;
}
class Area{
    private Integer id;
    private String name;
}
class Style{
    private Integer id;
    private String name;
}