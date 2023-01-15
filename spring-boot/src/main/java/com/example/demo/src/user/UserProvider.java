package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public User getUser(int id) throws BaseException {
        // 유저 id가 존재하는지 확인
        if(checkId(id) ==0){
            throw new BaseException(POST_USERS_NOT_EXISTS_ID);
        }

        try {
            User getUserRes = userDao.getUser(id);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    

    // 아이디 존재 여부 체크
    public int checkId(int id) throws BaseException{
        try{
            return userDao.checkId(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    // 이메일 중복 체크
    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 현재 이메일과 바꿀 이메일이 같은지 체킹
    public String checkSameEmail(int id) throws BaseException{
        try{
            return userDao.checkSameEmail(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 휴대전화 중복 체크
    public int checkPhone(String phone) throws BaseException{
        try{
            return userDao.checkPhone(phone);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }   
    // 바꾸고자 하는 연락처가 기존과 같은지 체크
    public String checkSamePhone(int id) throws BaseException{
        try{
            return userDao.checkSamePhone(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }   

    // 바꾸고자 하는 이름이 기존과 같은지 체크
    public String checkSameName(int id) throws BaseException{
        try{
            return userDao.checkSameName(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }   


    // 유저 상태 체크
    public char checkStatus(String email) throws BaseException{
        try{
            return userDao.checkStatus(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }   

    // 유저 광고동의 상태 체크
    public char checkCommercial(int id) throws BaseException{
        try{
            return userDao.checkCommercial(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }   

    // 유저 SMS/SNS 동의 상태 체크
    public char checkSms(int id) throws BaseException{
        try{
            return userDao.checkSms(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }   

    public char checkCemail(int id) throws BaseException{
        try{
            return userDao.checkCemail(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public char checkCpush(int id) throws BaseException{
        try{
            return userDao.checkCpush(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public char checkKakao(int id) throws BaseException{
        try{
            return userDao.checkKakao(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }    


    public PostLoginRes logIn(User loginUser) throws BaseException{

        /* 등록되지 않은 이메일 */
        if(checkEmail(loginUser.getEmail()) ==0){ 
            throw new BaseException(POST_USERS_NOT_EXISTS_EMAIL);
        }

        /* 탈퇴 회원 체크 */
        if(checkStatus(loginUser.getEmail())=='I'){ 
            throw new BaseException(POST_USERS_NOT_ACTIVATE);
        }

        GetPwdRes user = userDao.getPwd(loginUser); // password를 알아옴
        String encryptPwd;

        try {
            encryptPwd=new SHA256().encrypt(loginUser.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId,jwt);
        }
        else{
            // 비밀번호가 틀렸을때
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public PostLoginRes KakaologIn(User loginUser) throws BaseException{

        /* 탈퇴 회원 체크 */
        if(checkStatus(loginUser.getEmail())=='I'){ 
            throw new BaseException(POST_USERS_NOT_ACTIVATE);
        }

        GetPwdRes user = userDao.getPwd(loginUser); // password를 알아옴
        String encryptPwd;

        try {
            encryptPwd=new SHA256().encrypt(loginUser.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId,jwt);
        }
        else{
            // 비밀번호가 틀렸을때
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

}
