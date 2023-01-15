package com.example.demo.src.oauth;


import com.example.demo.src.user.model.*;
import com.example.demo.src.oauth.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OAuthDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createUser(User user){
        String createUserQuery = "insert into User (userName, email, password, phone) VALUES (?,?,?,29875456221)";
        Object[] createUserParams = new Object[]{user.getUserName(), user.getEmail(), user.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
   

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }


    public int checkId(int id){
        String checkIdQuery = "select exists(select userId from User where userId = ?)";
        int checkIdParams = id;
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                checkIdParams);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public String checkSameEmail(int id){
        String checkSameEmailQuery = "select email from User where userId = ?";
        int checkSameEmailParams = id;
        return this.jdbcTemplate.queryForObject(checkSameEmailQuery,
                String.class,
                checkSameEmailParams);
    }


    public char checkStatus(String email){
        String checkStatusQuery = "select status from User where email = ?";
        String checktStatusParams = email;

        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                char.class,
                checktStatusParams);
    }

    public char checkKakao(int id){
        String checkAdEamilQuery = "select kakao from User where userId = ?";
        int checkAdEamilParams = id;

        return this.jdbcTemplate.queryForObject(checkAdEamilQuery,
                char.class,
                checkAdEamilParams);
    }

    public GetPwdRes getPwd(User user){
        String getPwdQuery = "select userId,password from User where email = ?";
        String getPwdParams = user.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new GetPwdRes(
                        rs.getInt("userId"),
                        rs.getString("password")
                ),
                getPwdParams
                );

    }
}

