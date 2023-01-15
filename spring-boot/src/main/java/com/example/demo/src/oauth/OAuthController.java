// sudo ./gradlew clean build
// sudo java -jar build/libs/demo-0.0.1-SNAPSHOT.jar

// https://kauth.kakao.com/oauth/authorize?client_id=4b938b5ab35a8a20f7043e17acb7711b&redirect_uri=http://54.180.159.206:8001/oauth/kakao_front&response_type=code
package com.example.demo.src.oauth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.*;
import com.example.demo.src.user.model.*;
import com.example.demo.src.oauth.model.*;
import com.example.demo.src.oauth.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OAuthProvider oauthProvider;
    @Autowired
    private final OAuthDao oauthDao;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final OAuthService oauthService;


    public OAuthController(OAuthService oauthService,OAuthProvider oauthProvider, OAuthDao oauthDao, JwtService jwtService ){
        this.oauthDao = oauthDao;
        this.oauthProvider = oauthProvider;
        this.oauthService = oauthService;
        this.jwtService = jwtService;
    }
    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */
    @ResponseBody
    @GetMapping("/kakao_front") // 백엔드에서 소셜 로그인 test를 위한 것 
    public void kakaoCallback(@RequestParam String code) {
        System.out.println("kakao_code:"+code);
        System.out.println("===============OAuthService 함수 실행=========================");
        String access_token = oauthService.getKakaoAccessToken(code); // 인가코드를 넘겨주고, 인가토큰을 받아옴
        System.out.println("===============OAuthService 함수 끝=========================");
    }

    /**
     * 카카오 회원가입 or 로그인
     * [POST] /oauth/kakao
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/kakao") // 카카오 로그인 및 회원가입
    public BaseResponse<PostUserRes> createUser(@RequestBody PostKakaoinfoReq kakaoinfo) { //엑세스 토큰 입력받기

        User user = oauthService.KakaoGetInfo(kakaoinfo.getAccsessToken()); // 카카오 엑세스 토큰에서 정보 받아오기
        /* 벨리데이션 처리(유효하지 않은 토큰값이 들어왔을때)*/
        if(user.getUserName().equals("")){
            return new BaseResponse<>(POST_USERS_INVALIDE_ACCESSTOKEN); // 이름 입력이 없을때
        }   
        if(user.getEmail().equals("")){
            return new BaseResponse<>(POST_USERS_INVALIDE_ACCESSTOKEN); // 이메일 입력이 없을때
        }   
        if(user.getPhone().equals("")){
            return new BaseResponse<>(POST_USERS_INVALIDE_ACCESSTOKEN); // phone 입력이 없을때
        }   

        // 가입되지 않은 카카오 입력이라면, 회원가입 후 jwt, id 값 넘겨주기
        // 가입이 된 카카오 입력이라면, jwt, id값만 넘겨주기

        try{
            int choice_num = oauthService.choicekakao(user); //해당 함수에서 회원가입 or 로그인 선택
            if(choice_num == 0){ // 회원가입(완)
                 try{
                        PostUserRes postUserRes = oauthService.createUser(user);
                        return new BaseResponse<>(postUserRes);
                    } catch(BaseException exception){
                        return new BaseResponse<>((exception.getStatus()));
                    }
            }
            else{  // 로그인(진행)

                 try{
                        PostUserRes postUserRes = oauthProvider.logIn(user);
                        return new BaseResponse<>(postUserRes);
                    } catch(BaseException exception){
                        return new BaseResponse<>((exception.getStatus()));
                    }
                }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/test") // 백엔드에서 소셜 로그인 test를 위한 것 
    public void kakaoCallback() {
       
    }

}