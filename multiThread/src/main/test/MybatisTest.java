import entity.Res;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MybatisTest
 * @description:
 * @author: swm
 * @create: 2019-09-18 09:01
 **/

public class MybatisTest {
    private SqlSessionFactory sqlSessionFactory;
    @Test
    public void jdbcTest() throws SQLException {
        try {
            InputStream in = Resources.getResourceAsStream("config/mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSession sqlSession=sqlSessionFactory.openSession();
        System.out.println((Object) sqlSession.selectOne("videoResourcesMapper.select"));

        sqlSession.close();
    }
}

