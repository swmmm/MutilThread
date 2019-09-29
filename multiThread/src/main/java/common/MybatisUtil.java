package common;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: MybatisUtil
 * @description:
 * @author: swm
 * @create: 2019-09-23 14:53
 **/
public class MybatisUtil {
    public static SqlSession sqlSession;
    static  {
        InputStream in = null;
        try {
            in = Resources.getResourceAsStream("config/mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = sqlSessionFactory.openSession();
    }

}
