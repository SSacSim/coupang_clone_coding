// sudo ./gradlew clean build
// sudo java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
package com.example.demo.src.mailoauth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import com.example.demo.src.mailoauth.*;
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
import org.springframework.stereotype.Controller;



@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private final MailService mailService;
    private final UserProvider userProvider;

    public MailController(MailService mailService, UserProvider userProvider){
        this.mailService = mailService;
         this.userProvider = userProvider;
    }
    
    // 인증 email 보내기 ( 인증 테이블에 삽입)
    @PostMapping("/oauth") // 메일을 보내면 
    public BaseResponse<OauthIdRes> execMail(@RequestBody User user) {
        try{
            OauthIdRes result = mailService.mailSend(user.getEmail());
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // 인증 번호 입력 확인
    @PostMapping("/oauth-check") // 메일을 보내면 
    public BaseResponse<String> execMail(@RequestBody OauthRes oauthRes) { // ouath 번호와 인증 번호 body로 받기
        try{
            mailService.mailNumCheck(oauthRes);

            String result = "";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users/sign-up
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<PostUserRes> createUser(@RequestBody OauthUser user) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        
        
        /*====================================== 이메일 인증 체크 */
        if(user.getUserOauthId() == 0){
            return new BaseResponse<>(POST_OAUTH_MISS_INFO); // 인증 ID가 없을경우.
        }

        /*====================================== 인증 번호 입력 체크 */
        if(user.getOauthNum() == null){
            return new BaseResponse<>(POST_OAUTH_MISS_NUM); // 인증 번호가 없을경우.
        }

        /*===================================== 이름 체크 */
        if(user.getUserName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NAME); // 이름의 입력이 없을때
        }

        if(!isRegexKoreanWord(user.getUserName())){
            return new BaseResponse<>(POST_USERS_WANT_CORRET_NAME); // 한글 자음,모음만 들어갔는지 체크
        }

        if(user.getUserName().length() <=1){
            return new BaseResponse<>(POST_USERS_WANT_CORRET_NAME); // 이름이 두글자 이상인지 체크
        }

        /*===================================== 이메일 체크 */
        if(user.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL); // Email 입력이 없을때
        }

        //이메일 정규표현
        if(!isRegexEmail(user.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL); // Email 형식이 맞는지 확인
        }

        /*===================================== 패스워드 체크*/
        
        // 패스워드 입력이 없을때
        if(user.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD); 
        }

         //비밀번호와 이메일이 같은경우
        if(user.getEmail().split("@")[0].equals(user.getPassword())){
            return new BaseResponse<>(POST_USERS_SAME_EMAIL_PASSWORD); 
        }

        // 패스워드 길이 체크
        int password_len = user.getPassword().length();
        if(password_len < 8 | password_len > 20){ // 연락처 길이 체크
            return new BaseResponse<>(POST_USERS_LENGTH_PASSWORD); 
        }

        //동일한 문자/숫자 체크
        if(isRegexSameword(user.getPassword())){
            return new BaseResponse<>(POST_USERS_3TIME_PASSWORD); 
        }

        //연속된 문자/숫자 체크
        if(isRegexcontinuity(user.getPassword())){
            return new BaseResponse<>(POST_USERS_CONTINUITY_PASSWORD); 
        }

        //입력하면 안되는 특수문자 체크 
        if(isRegexInvalidationWord(user.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_WORD_PASSWORD); 
        }   

        /*==================================== 연락처 체크 */

        if(user.getPhone() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE); 
        }

        // 휴대폰 길이 확인 0103334444 or 01044445555 이런 형식만 존재, 10~11 자리인지 확인
        int phone_len = user.getPhone().length();
        if(!isRegexPhone(user.getPhone())){ // 연락처 숫자만 들어왔나 체크 
            return new BaseResponse<>(POST_USER_INVALID_PHONE); 
        }

        if(phone_len < 10 | phone_len > 11){ // 연락처 길이 체크
            return new BaseResponse<>(POST_USER_INVALID_PHONE); 
        }
        /* =================================== 회원가입 validation 형식적 처리 끝 */ 

        try{
            PostUserRes postUserRes = mailService.createUser(user);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}