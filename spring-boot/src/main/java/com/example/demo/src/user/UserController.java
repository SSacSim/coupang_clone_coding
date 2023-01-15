// sudo ./gradlew clean build
// sudo java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
// feat: Add sign-up API

// sudo git pull origin dev
// sudo git push origin bert:dev

package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }


    /**
     * 회원 1명 조회 API
     * [GET] /users/:userId
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<User> getUser(@PathVariable("userId") int userId) {
        // Get Users
        try{

            int userIdByJwt = jwtService.getUserId(); // jwt는 프론트에서 준 토큰 값으로 id 값을 추출해옴

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            User getUserRes = userProvider.getUser(userId);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
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
    public BaseResponse<PostUserRes> createUser(@RequestBody User user) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!


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


        try{
            PostUserRes postUserRes = userService.createUser(user);

            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")   // email , password 두개만 받아 로그인
    public BaseResponse<PostLoginRes> logIn(@RequestBody User user){ 

        /*===================================== email 형식 체크*/
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

        try{

            PostLoginRes postLoginRes = userProvider.logIn(user);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
  
            return new BaseResponse<>(exception.getStatus());
        }
    }


    //==============================================PATCH=====================================================
    /* 4.email 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/email") 
    public BaseResponse<String> modifyEmail(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {


            // 이메일 입력이 없을때
            if(user.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL); // Email 입력이 없을때
            }   
            //이메일 정규표현 체킹
            if(!isRegexEmail(user.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL); // Email 형식이 맞는지 확인
            }

            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
        
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyEmail(user);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    

    /* 5.phone 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/phone") 
    public BaseResponse<String> modifyPhone(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {


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

            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();


            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 phone 변경
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyPhone(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 6.password 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/password") 
    public BaseResponse<String> modifyPassword(@PathVariable("userId") int userId, @RequestBody UserPassword user){ 
        try {


             /*===================== 패스워드 체크*/
            // 패스워드 입력이 없을때 하나라도 들어오지 않았을때
            if(user.getNowPassword() == null || user.getNewPassword() == null ||user.getCheckPassword() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD); 
            }

            // 패스워드 길이 체크
            int password_len = user.getNewPassword().length();
            if(password_len < 8 | password_len > 20){ // 연락처 길이 체크
                return new BaseResponse<>(POST_USERS_LENGTH_PASSWORD); 
            }

            //동일한 문자/숫자 체크
            if(isRegexSameword(user.getNewPassword())){
                return new BaseResponse<>(POST_USERS_3TIME_PASSWORD); 
            }

            //연속된 문자/숫자 체크
            if(isRegexcontinuity(user.getNewPassword())){
                return new BaseResponse<>(POST_USERS_CONTINUITY_PASSWORD); 
            }

            //입력하면 안되는 특수문자 체크 
            if(isRegexInvalidationWord(user.getNewPassword())){
                return new BaseResponse<>(POST_USERS_INVALID_WORD_PASSWORD); 
            }   

            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();


            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 비밀번호 변경
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyPassword(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {

            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /* 7.commercial 수신동의 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/commercial") 
    public BaseResponse<String> modifyCommercial(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {

            // 필수 정보가 들어오지 않았을때
            if(user.getAgreeInfo() == null){
                return new BaseResponse<>(POST_USERS_MISS_INFO);
            }

            if(user.getAgreeInfo().length() != 1){ // 여러 글자가 들어오면 안되서
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }

            if(user.getAgreeInfo().charAt(0) !='N' & user.getAgreeInfo().charAt(0) !='Y' ){ // N or Y 이외의 글자가 들어올때
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }

            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 정보 변경
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyCommercial(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /* 8.ad-sms 수신동의 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/ad-sms") 
    public BaseResponse<String> modifyCsms(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {

            // 필수 정보가 들어오지 않았을때
            if(user.getAdSms() == null){
                return new BaseResponse<>(POST_USERS_MISS_INFO);
            }

            if(user.getAdSms().length() != 1){ // 여러 글자가 들어오면 안되서
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }

            if(user.getAdSms().charAt(0) !='N' & user.getAdSms().charAt(0) !='Y' ){ // N or Y 이외의 글자가 들어올때
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }
            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 정보 변경
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyCsms(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 9.ad-email 수신동의 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/ad-email") 
    public BaseResponse<String> modifyCemail(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {

            // 필수 정보가 들어오지 않았을때
            if(user.getAdEmail() == null){
                return new BaseResponse<>(POST_USERS_MISS_INFO);
            }
            if(user.getAdEmail().length() != 1){ // 여러 글자가 들어오면 안되서
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }

            if(user.getAdEmail().charAt(0) !='N' & user.getAdEmail().charAt(0) !='Y' ){ // N or Y 이외의 글자가 들어올때
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }

            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            

            //같다면 정보 변경
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyCemail(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 10.ad-push 수신동의 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/ad-app-push") 
    public BaseResponse<String> modifyCpush(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {

            // 필수 정보가 들어오지 않았을때
            if(user.getAdAppPush() == null){
                return new BaseResponse<>(POST_USERS_MISS_INFO);
            }
            if(user.getAdAppPush().length() != 1){ // 여러 글자가 들어오면 안되서
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }

            if(user.getAdAppPush().charAt(0) !='N' & user.getAdAppPush().charAt(0) !='Y' ){ // N or Y 이외의 글자가 들어올때
                return new BaseResponse<>(POST_USERS_MISS_INPUT);
            }


            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 정보 변경
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyCpush(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 11.profile 프로필 사진 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/profile") 
    public BaseResponse<String> modifyProfile(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {
            
            // 필수 정보가 들어오지 않았을때
            if(user.getProfileImgUrl() == null){
                return new BaseResponse<>(POST_USERS_MISS_INFO);
            }

            // 확장자명 체크
            if(!isRegexImg(user.getProfileImgUrl())){
                return new BaseResponse<>(POST_USERS_NOT_USER_IMG);
            }


            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 정보 변경
            user.setUserId(userId); // userId는 받아올 필요 x
            userService.modifyProfile(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 13.userName 변경 */
    @ResponseBody
    @PatchMapping("/{userId}/name") 
    public BaseResponse<String> modifyName(@PathVariable("userId") int userId, @RequestBody User user){ 
        try {
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

            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 정보 변경
            user.setUserId(userId); 
            userService.modifyName(user);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


   //==============================================DELETE=====================================================
    /* 12.유저 삭제*/
    @ResponseBody
    @DeleteMapping("/{userId}") 
    public BaseResponse<String> modifyDel(@PathVariable("userId") int userId){ 
        try {
            //jwt에서 id 추출.
            int userIdByJwt = jwtService.getUserId(); // jwt는 프론트에서 준 토큰 값으로 id 값을 추출해옴

            //userId와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.modifyDel(userId);

            String result = "";

        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}