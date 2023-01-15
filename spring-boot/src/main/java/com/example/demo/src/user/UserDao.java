package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public User getUser(int userIdx){
        String getUserQuery = "select * from User where userId = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("wowMem"),
                        rs.getInt("couPay"),
                        rs.getInt("couCash"),
                        rs.getString("profileImgUrl"),
                        rs.getString("agreeInfo"),
                        rs.getString("adEmail"),
                        rs.getString("adSms"),
                        rs.getString("adAppPush")
                        ),
                getUserParams);
    }
    

    public int createUser(User user){
        String createUserQuery = "insert into User (userName, email, password, phone, agreeInfo, adEmail, adSms, adAppPush) VALUES (?,?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{user.getUserName(), user.getEmail(), user.getPassword(), user.getPhone(), user.getAgreeInfo(), user.getAdEmail(), user.getAdSms(), user.getAdAppPush()};
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

    public int checkPhone(String phone){
        String checkPhoneQuery = "select exists(select phone from User where phone = ?)";
        String checkPhoneParams = phone;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public String checkSamePhone(int id){
        String checkSamePhoneQuery = "select phone from User where userId = ?";
        int checkSamePhoneParams = id;
        return this.jdbcTemplate.queryForObject(checkSamePhoneQuery,
                String.class,
                checkSamePhoneParams);
    }

    public String checkSameName(int id){
        String checkSameNameQuery = "select userName from User where userId = ?";
        int checkSameNameParams = id;
        return this.jdbcTemplate.queryForObject(checkSameNameQuery,
                String.class,
                checkSameNameParams);
    }

    public char checkStatus(String email){
        String checkStatusQuery = "select status from User where email = ?";
        String checktStatusParams = email;

        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                char.class,
                checktStatusParams);
    }

    public char checkCommercial(int id){
        String checkCommercialQuery = "select agreeInfo from User where userId = ?";
        int checkCommercialParams = id;

        return this.jdbcTemplate.queryForObject(checkCommercialQuery,
                char.class,
                checkCommercialParams);
    }

    public char checkSms(int id){
        String checkSmsQuery = "select adSms from User where userId = ?";
        int checkSmsParams = id;

        return this.jdbcTemplate.queryForObject(checkSmsQuery,
                char.class,
                checkSmsParams);
    }

    public char checkCemail(int id){
        String checkAdEamilQuery = "select adEmail from User where userId = ?";
        int checkAdEamilParams = id;

        return this.jdbcTemplate.queryForObject(checkAdEamilQuery,
                char.class,
                checkAdEamilParams);
    }

    public char checkCpush(int id){
        String checkPushQuery = "select adAppPush from User where userId = ?";
        int checkPushParams = id;

        return this.jdbcTemplate.queryForObject(checkPushQuery,
                char.class,
                checkPushParams);
    }

    public String checkPassword(int id){
        String checkPwdQuery = "select password from User where userId = ?";
        int checkPwdParams = id;
        return this.jdbcTemplate.queryForObject(checkPwdQuery,
                String.class,
                checkPwdParams);
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

    /*  PATCH Dao */
    public int modifyEmail(User user){
        String modifyEmailQuery = "update User set email = ? where userId = ? ";
        Object[] modifyEmailParams = new Object[]{user.getEmail(), user.getUserId()};
        return this.jdbcTemplate.update(modifyEmailQuery,modifyEmailParams);
    }

    public int modifyPhone(User user){
        String modifyPhoneQuery = "update User set phone = ? where userId = ? ";
        Object[] modifyPhoneParams = new Object[]{user.getPhone(), user.getUserId()};
        return this.jdbcTemplate.update(modifyPhoneQuery,modifyPhoneParams);
    }

    public int modifyPassword(UserPassword user){
        String modifyPwdQuery = "update User set password = ? where userId = ? ";
        Object[] modifyPwdParams = new Object[]{user.getNewPassword(), user.getUserId()};
        return this.jdbcTemplate.update(modifyPwdQuery,modifyPwdParams);
    }

    public int modifyCommercial(User user){
        String modifyCommercialQuery = "update User set agreeInfo = ? where userId = ? ";
        Object[] modifyCommercialParams = new Object[]{user.getAgreeInfo(), user.getUserId()};
        return this.jdbcTemplate.update(modifyCommercialQuery,modifyCommercialParams);
    }

    public int modifyCsms(User user){
        String modifyCsmsQuery = "update User set adSms = ? where userId = ? ";
        Object[] modifyCsmsParams = new Object[]{user.getAdSms(), user.getUserId()};
        return this.jdbcTemplate.update(modifyCsmsQuery,modifyCsmsParams);
    }

    public int modifyCemail(User user){
        String modifyCemailQuery = "update User set adEmail = ? where userId = ? ";
        Object[] modifyCemailParams = new Object[]{user.getAdEmail(), user.getUserId()};
        return this.jdbcTemplate.update(modifyCemailQuery,modifyCemailParams);
    }

    public int modifyCpush(User user){
        String modifyCpushQuery = "update User set adAppPush = ? where userId = ? ";
        Object[] modifyCpushParams = new Object[]{user.getAdAppPush(), user.getUserId()};
        return this.jdbcTemplate.update(modifyCpushQuery,modifyCpushParams);
    }

    public int modifyProfile(User user){
        String modifyProfileQuery = "update User set profileImgUrl = ? where userId = ? ";
        Object[] modifyProfileParams = new Object[]{user.getProfileImgUrl(), user.getUserId()};
        return this.jdbcTemplate.update(modifyProfileQuery,modifyProfileParams);
    }


    public int modifyDel(int userId){
        String modifyDelQuery = "update User set status = ? where userId = ? ";
        Object[] modifyDelParams = new Object[]{"I",userId}; // del에서는 무조건 I(nactive)로 변경
        return this.jdbcTemplate.update(modifyDelQuery,modifyDelParams);
    }

    public int modifyName(User user){
        String modifyNameQuery = "update User set userName = ? where userId = ? ";
        Object[] modifyNameParams = new Object[]{user.getUserName(),user.getUserId()}; // del에서는 무조건 I(nactive)로 변경
        return this.jdbcTemplate.update(modifyNameQuery,modifyNameParams);
    }


    public int modifyKaKao(int userId){
        String modifyKakaoQuery = "update User set kakao = ? where userId = ? ";
        Object[] modifyKakaoParams = new Object[]{"Y",userId}; 
        return this.jdbcTemplate.update(modifyKakaoQuery,modifyKakaoParams);
    }
}

