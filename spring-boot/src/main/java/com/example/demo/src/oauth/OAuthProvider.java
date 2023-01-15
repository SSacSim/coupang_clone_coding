package com.example.demo.src.oauth;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.src.oauth.model.*;
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
public class OAuthProvider {

    private final OAuthDao oauthDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OAuthProvider(OAuthDao oauthDao, JwtService jwtService) {
        this.oauthDao = oauthDao;
        this.jwtService = jwtService;
    }
    
    // 아이디 존재 여부 체크
    public int checkId(int id) throws BaseException{
        try{
            return oauthDao.checkId(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    // 이메일 중복 체크
    public int checkEmail(String email) throws BaseException{
        try{
            return oauthDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
   

    // 유저 상태 체크
    public char checkStatus(String email) throws BaseException{
        try{
            return oauthDao.checkStatus(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }   


    public char checkKakao(int id) throws BaseException{
        try{
            return oauthDao.checkKakao(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }    


    public PostUserRes logIn(User loginUser) throws BaseException{

        /* 탈퇴 회원 체크 */
        if(checkStatus(loginUser.getEmail())=='I'){ 
            throw new BaseException(POST_USERS_NOT_ACTIVATE);
        }

        GetPwdRes user = oauthDao.getPwd(loginUser); // password를 알아옴
        String encryptPwd;

        try {
            encryptPwd=new SHA256().encrypt(loginUser.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt,userId);
        }
        else{
            // 비밀번호가 틀렸을때
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

}
