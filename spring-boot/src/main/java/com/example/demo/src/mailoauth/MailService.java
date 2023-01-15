package com.example.demo.src.mailoauth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;

import com.example.demo.src.mailoauth.*;
import com.example.demo.src.mailoauth.model.*;
import com.example.demo.src.user.*;
import com.example.demo.src.user.model.*;
import com.example.demo.src.mailoauth.model.*;

import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.*;

import com.example.demo.src.mailoauth.MailDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;
@Service
@AllArgsConstructor
public class MailService {


    private final JavaMailSender mailSender;
    private final MailDao maildao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public MailService(MailDao maildao, UserProvider userProvider, JwtService jwtService , JavaMailSender mailSender) {
        this.maildao = maildao;
        this.userProvider = userProvider;
        this.mailSender = mailSender;
        this.jwtService = jwtService;
    }

    public OauthIdRes mailSend(String email) throws BaseException{


        // DB에서 이미 가입했는지 확인
        if(userProvider.checkEmail(email) ==1){ // 이미 등록된 email
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        // 이미 인증을 요청했는지 확인
        if(maildao.checkAlreadySend(email) == 1){
            throw new BaseException(POST_OAUTH_ALREADY_SEND);
        }

        SimpleMailMessage message = new SimpleMailMessage();
        
        int oauthNum = new Random().nextInt(9999999);

        int oauthId = maildao.createOauth(email, oauthNum);

        
        message.setTo(email); // 인증메일을 받을 사람(회원 가입을 하고자 하는 이메일)
    
        message.setFrom("ssac_sim@naver.com"); // 인증 메일을 보내는 사람(바꾸기 x)
 
        message.setSubject("mr-coupang 인증번호"); // 제목

        message.setText("인증번호 :"+ oauthNum); // 인증번호와 첨부할 내용

        mailSender.send(message); // 이메일 보내기

        return new OauthIdRes(oauthId);
    }

    public void mailNumCheck(OauthRes oauthRes) throws BaseException{

        if( maildao.checkOauth(oauthRes.getOuathId(),oauthRes.getResOauthNum()) == 0 ){
             throw new BaseException(POST_OAUTH_INVALID);
        }
        // id와 인증번호가 같은게 있음. > body로 들어온 인증번호와 같은지 체킹
        if( !(maildao.checkOauthNum(oauthRes.getOuathId()).equals(oauthRes.getResOauthNum()))){
            throw new BaseException(POST_OAUTH_FAIL);
        }

        // 이미 인증이 되었는지 체킹
        if( maildao.checkOauthStatus(oauthRes.getOuathId()) =='Y'){
            throw new BaseException(POST_OAUTH_ALREADY_PASS);
        }
        // update oauthCheck
        maildao.modifyOauthCheck(oauthRes.getOuathId());

    }
    // POST
    public PostUserRes createUser(OauthUser user) throws BaseException {

        /* 인증 아이디가 유효한지 확인 */
        if( maildao.checkValidNum(user.getUserOauthId()) == 0 ){ 
            throw new BaseException(POST_OAUTH_INVALID_OAUTHNUM);
        }

        /* 보낸 인증번호와 입력한 인증 번호와 같은지 확인*/
        if(!(maildao.getOauthNum(user.getUserOauthId()).equals(user.getOauthNum())) ){ 
            throw new BaseException(POST_OAUTH_NOT_SAME_OAUTHNUM);
        }

        /* 인증을 요청한 이메일과, 인증ID에 이메일이 같은지 확인*/
        if(!(maildao.getEmail(user.getUserOauthId()).equals(user.getEmail())) ){ 
            throw new BaseException(POST_OAUTH_INVALID_EMAIL);
        }
        /* 이메일 인증을 받지 않은 유저일 경우 */
        if(maildao.checkOauthStatus(user.getUserOauthId()) =='N'){ 
            throw new BaseException(POST_OAUTH_NOT_PASS);
        }

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

            int userIdx = maildao.createUser(user);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}