package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(User user) throws BaseException {
        //이메일 중복 체크
        if(userProvider.checkEmail(user.getEmail()) ==1){ // 이미 등록된 email
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        if(userProvider.checkPhone(user.getPhone()) ==1){ // 이미 등록된 phone
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(user.getPassword());
            user.setPassword(pwd); // jwt 값 설정

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(user);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* --------------------------- PATCH */

    public void modifyEmail(User user) throws BaseException {
        

        // 바꾸는 email과 현재 email이 같을 경우 예외처리
        if(userProvider.checkSameEmail(user.getUserId()).equals(user.getEmail())){ // 이미 등록된 email
            throw new BaseException(POST_USERS_SAME_EMAIL);
        }

        // 이미 등록된 이메일
        if(userProvider.checkEmail(user.getEmail()) ==1){ 
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        try{
            int result = userDao.modifyEmail(user);
            if(result == 0){ // 
                throw new BaseException(MODIFY_FAIL_EMAIL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void modifyPhone(User user) throws BaseException {

        // 바꾸는 phone과 현재 phone이 같을 경우 예외처리
        if(userProvider.checkSamePhone(user.getUserId()).equals(user.getPhone())){ 
            throw new BaseException(POST_USERS_SAME_PHONE);
        }

        // 이미 등록된 phone
        if(userProvider.checkPhone(user.getPhone()) ==1){ 
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

        try{
            int result = userDao.modifyPhone(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PHONE);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyPassword(UserPassword user) throws BaseException {
        // 입력한 현재 비밀번호가 일치하는지 확인
        if( !( userDao.checkPassword(user.getUserId()).equals( new SHA256().encrypt(user.getNowPassword())) )){
            throw new BaseException(POST_USERS_NOT_COLLECT_PASSWORD); 
        }
        
        //비밀번호와 이메일이 같은경우
        if(userDao.checkSameEmail(user.getUserId()).split("@")[0].equals(user.getNewPassword())){
            throw new BaseException(POST_USERS_SAME_EMAIL_PASSWORD); 
        }

        //기존 비밀 번호와 바꾸는 비밀번호가 같은지 확인
        if( user.getNowPassword().equals(user.getNewPassword()) ){
            throw new BaseException(POST_USERS_SAME_PASSWORD); 
        }
        
        // 비밀번호 2차 확인( 바꾸는 비밀번호와 재입력 비밀번호가 같은지 확인)
        if( !(user.getNewPassword().equals(user.getCheckPassword()) )){
            throw new BaseException(POST_USERS_CHECK_PASSWORD); 
        }

        try{

            try{
                //암호화
                String changePwd;
                changePwd = new SHA256().encrypt(user.getNewPassword());
                user.setNewPassword(changePwd); // 바꾸는 비밀번호 set

                } catch (Exception ignored) {
                    throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
                }

            // 실질적으로 비밀번호를 변경하는 곳
            int result = userDao.modifyPassword(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PASSWORD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCommercial(User user) throws BaseException {

        // 기존상태와 바꾸고자 하는 상태가 같은가?
        if(userProvider.checkCommercial(user.getUserId()) == user.getAgreeInfo().charAt(0) ){ 
            throw new BaseException(POST_USERS_SAME_STATUS);
        }

        try{
            // 실질적 정보 변경
            int result = userDao.modifyCommercial(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_COMMERCIAL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCsms(User user) throws BaseException {

        // 기존상태와 바꾸고자 하는 상태가 같은가?
        if(userProvider.checkSms(user.getUserId()) == user.getAdSms().charAt(0) ){ 
            throw new BaseException(POST_USERS_SAME_STATUS);
        }
        
        try{
            // 실질적 정보 변경
            int result = userDao.modifyCsms(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_CSMS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCemail(User user) throws BaseException {

        // 기존상태와 바꾸고자 하는 상태가 같은가?
        if(userProvider.checkCemail(user.getUserId()) == user.getAdEmail().charAt(0) ){ 
            throw new BaseException(POST_USERS_SAME_STATUS);
        }

        try{

            // 실질적 정보 변경
            int result = userDao.modifyCemail(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_CEMAIL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCpush(User user) throws BaseException {
        // 기존상태와 바꾸고자 하는 상태가 같은가?
        if(userProvider.checkCpush(user.getUserId()) == user.getAdAppPush().charAt(0) ){ 
            throw new BaseException(POST_USERS_SAME_STATUS);
        }
        try{
            // 실질적 정보 변경
            int result = userDao.modifyCpush(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_APPPUSH);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProfile(User user) throws BaseException {
        try{
            // 실질적 정보 변경
            int result = userDao.modifyProfile(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PROFILE);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyDel(int userId) throws BaseException {
        try{
            // 실질적 정보 변경
            int result = userDao.modifyDel(userId);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_DEL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyName(User user) throws BaseException {

        // 바꾸는 이름과 현재 이름이 같을 경우 예외처리
        if(userProvider.checkSameName(user.getUserId()).equals(user.getUserName())){ 
            throw new BaseException(POST_USERS_SAME_NAME);
        }
        try{

            // 실질적 정보 변경
            int result = userDao.modifyName(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
}