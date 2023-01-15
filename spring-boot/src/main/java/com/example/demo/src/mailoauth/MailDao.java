package com.example.demo.src.mailoauth;


import com.example.demo.src.user.model.*;
import com.example.demo.src.mailoauth.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MailDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createUser(OauthUser user){
        String createUserQuery = "insert into User (userName, email, password, phone, agreeInfo, adEmail, adSms, adAppPush) VALUES (?,?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{user.getUserName(), user.getEmail(), user.getPassword(), user.getPhone(), user.getAgreeInfo(), user.getAdEmail(), user.getAdSms(), user.getAdAppPush()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
   

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int createOauth(String email, int oauthNum){
        String createOauthuserQuery = "insert into OauthEmail (email, oauthNum) VALUES (?,?)";
        Object[] createOauthuserParams = new Object[]{email, oauthNum};
        this.jdbcTemplate.update(createOauthuserQuery, createOauthuserParams);
   

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

    public int checkOauth(int oauthId, String oauthNum){
        String checkEmailQuery = "select exists(select oauthNum from OauthEmail where oeId = ? )";
        int checkEmailParams = oauthId;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    

    public int modifyOauthCheck(int oauthId){
        String modifyOauthCheckQuery = "update OauthEmail set oauthCheck = ? where oeId = ? ";
        Object[] modifyOauthCheckParams = new Object[]{"Y", oauthId};
        return this.jdbcTemplate.update(modifyOauthCheckQuery,modifyOauthCheckParams);
    }

    public String checkOauthNum(int oauthId){
        String checkOauthNumQuery = "select oauthNum from OauthEmail where oeId = ?";
        int checkOauthNumParams = oauthId;
        return this.jdbcTemplate.queryForObject(checkOauthNumQuery,
                String.class,
                checkOauthNumParams);
    }



    public char checkOauthStatus(int oauthId){
        String checkOautStatusQuery = "select oauthCheck from OauthEmail where oeId = ?";
        int checkOautStatusParams = oauthId;
        return this.jdbcTemplate.queryForObject(checkOautStatusQuery,
                char.class,
                checkOautStatusParams);
    }

    public int checkAlreadySend(String email){
        String checkSendQuery = "select exists(select oeId from OauthEmail where email = ? and oauthCheck = ?)";
        Object[] checkSendParams = new Object[]{email,"N"};
        return this.jdbcTemplate.queryForObject(checkSendQuery,
                int.class,
                checkSendParams);
    }

    public String getEmail(int oauthId){
        String getEmailQuery = "select email from OauthEmail where oeId = ?";
        int getEmailParams = oauthId;
        return this.jdbcTemplate.queryForObject(getEmailQuery,
                String.class,
                getEmailParams);
    }

    public String getOauthNum(int oauthId){
        String getOauthNumQuery = "select oauthNum from OauthEmail where oeId = ? ";
        int getOauthNumParams = oauthId;
        return this.jdbcTemplate.queryForObject(getOauthNumQuery,
                String.class,
                getOauthNumParams);
    }

    public int checkValidNum(int oauthId){
        String checkValidQuery = "select exists(select oauthNum from OauthEmail where oeId = ? )";
        int checkValidParams = oauthId;
        return this.jdbcTemplate.queryForObject(checkValidQuery,
                int.class,
                checkValidParams);
    }
}