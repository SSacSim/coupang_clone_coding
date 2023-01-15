package com.example.demo.src.oauth;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OAuthService{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OAuthDao oauthDao;
    private final OAuthProvider oauthProvider;
    private final JwtService jwtService;


    @Autowired
    public OAuthService(OAuthDao oauthDao, OAuthProvider oauthProvider, JwtService jwtService) {
        this.oauthDao = oauthDao;
        this.oauthProvider = oauthProvider;
        this.jwtService = jwtService;
    }

    public String getKakaoAccessToken (String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=4b938b5ab35a8a20f7043e17acb7711b"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=https://prod.mr-coupang.shop/oauth/kakao_front"); // TODO 인가코드 받은 redirect_uri 입력 (찐 서버)
            // sb.append("&redirect_uri=http://54.180.159.206:8001/oauth/kakao_front"); // dev서버
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public User KakaoGetInfo(String access_token) { // 유저 정보 알아오기
        String id ="";
        String nickname="";
        String profileImg ="";
        String email="";
        try {
            String reqURL = "https://kapi.kakao.com/v2/user/me";

            //access_token을 이용하여 사용자 정보 조회
        
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + access_token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            
            
            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            id = element.getAsJsonObject().get("id").getAsString();
            nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            profileImg = element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();
            email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();

            
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new User(-1,nickname,"@kakao"+email,email+"!@#",id,"N",0,0,profileImg,"N","N","N","N");
    }

    /*========================================== 카카오를 위한 ==================*/
    // 회원 가입인지 로그인인지 확인
    public int choicekakao(User user) throws BaseException { 
        try{
            int choiceNum = 0;
            // choiceNum이 0이면 회원가입/ 1이면 로그인
            if(oauthDao.checkEmail(user.getEmail()) ==1){
                choiceNum =1;
            }
            return choiceNum;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserRes createUser(User user) throws BaseException {
        
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(user.getPassword());
            user.setPassword(pwd); // jwt 값 설정

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = oauthDao.createUser(user);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx); // 회원 가입시 jwt값과 index 값 돌려줌
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}