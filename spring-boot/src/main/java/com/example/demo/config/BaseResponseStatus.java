package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users

    /* 이름 관련 100~*/
    POST_USERS_EMPTY_NAME(false, 2100, "이름을 입력해주세요."),
    POST_USERS_WANT_CORRET_NAME(false, 2101, "이름을 정확히 입력하세요."),
    POST_USERS_SAME_NAME(false, 2102, "현재 이름과 바꾸는 이름이 동일합니다."),

    /* 이메일 관련 200~*/
    POST_USERS_EMPTY_EMAIL(false, 2200, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2201, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2202,"중복된 이메일입니다."),
    POST_USERS_NOT_EXISTS_EMAIL(false,2203,"등록되지 않은 이메일입니다."),
    POST_USERS_SAME_EMAIL(false,2204,"현재 사용하는 이메일과 같습니다."),
    POST_OAUTH_INVALID(false,2205,"해당 이메일로 인증번호를 요청하지 않았습니다."),
    POST_OAUTH_FAIL(false,2206,"인증번호가 다릅니다."),
    POST_OAUTH_ALREADY_PASS(false,2207,"이미 인증되었습니다."),
    POST_OAUTH_ALREADY_SEND(false,2208,"이미 인증번호를 요청하였습니다."),
    POST_OAUTH_MISS_INFO(false,2209,"인증 아이디를 입력해주세요."),
    POST_OAUTH_NOT_PASS(false,2210,"이메일 인증을 먼저 진행해주세요."),
    POST_OAUTH_INVALID_EMAIL(false,2211,"유효하지 않은 이메일입니다."),
    POST_OAUTH_MISS_NUM(false,2212,"인증 번호를 입력해주세요."),
  
    /* 패스워드 관련 300~*/
    POST_USERS_EMPTY_PASSWORD(false, 2300,"비밀번호를 입력해주세요."),
    POST_USERS_LENGTH_PASSWORD(false,2301,"비밀번호는 8~20글자로 지정해주세요."), 
    POST_USERS_SAME_EMAIL_PASSWORD(false,2302,"패스워드에 이메일을 사용할 수 없습니다."),
    POST_USERS_3TIME_PASSWORD(false,2303,"3개이상 동일한 문자/숫자는 사용할 수 없습니다."),
    POST_USERS_CONTINUITY_PASSWORD(false,2304,"3개이상 연속된 문자/숫자는 사용할 수 없습니다."),
    POST_USERS_INVALID_WORD_PASSWORD(false,2305,"비밀번호에 사용할 수 없는 문자가 포함되어 있습니다."),
    POST_USERS_NOT_COLLECT_PASSWORD(false,2306,"비밀번호가 틀렸습니다."),
    POST_USERS_SAME_PASSWORD(false,2307,"기존 비밀번호와 같습니다."),
    POST_USERS_CHECK_PASSWORD(false,2308,"바꾸려는 비밀번호가 서로 다릅니다."),

    /* 연락처 관련 400~*/
    POST_USERS_EMPTY_PHONE(false, 2400,"연락처를 입력해주세요."),
    POST_USER_INVALID_PHONE(false, 2401, "연락처 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE(false, 2402, "중복된 연락처입니다."),
    POST_USERS_SAME_PHONE(false,2403,"현재 사용하는 연락처와 동일합니다."),

    /* 수신동의 관련 500~*/
    POST_USERS_SAME_STATUS(false,2500,"현재 상태와 바꾸고자 하는 상태가 동일합니다."),
    POST_USERS_MISS_INFO(false,2501,"필수 입력데이터가 누락됐습니다."),
    POST_USERS_MISS_INPUT(false,2502,"입력 형식이 잘못됐습니다."),

    /* 공통 600~*/
    POST_USERS_USE_BLANK(false,2600,"공백이 들어갈 수 없습니다."),
    POST_USERS_NOT_ACTIVATE(false, 2601, "탈퇴한 회원입니다."),
    POST_USERS_NOT_EXISTS_ID(false, 2602, "해당 유저가 없습니다."),
    POST_USERS_NOT_USER_IMG(false, 2603, "jpg(jpeg), png 파일을 이용해주세요."),
    POST_USERS_INVALIDE_ACCESSTOKEN(false, 2604, "유효하지 않은 토큰입니다."),
    POST_OAUTH_INVALID_OAUTHNUM(false,2605,"유효하지 않은 인증아이디입니다."),
    POST_OAUTH_NOT_SAME_OAUTHNUM(false,2606,"인증번호가 일치하지 않습니다."),
    
    

    INVALID_CATEGORYID_ERROR(false, 2029, "조회할 수 없는 카테고리입니다."),
    INVALID_ITEMID_ERROR(false, 2030, "조회할 수 없는 상품입니다."),


    DUPLICATED_ITEM_WISH(false, 2031, "이미 찜한 상품입니다."),
    INVALID_WISH_ITEM(false, 2032, "찜한 상품이 아닙니다."),

    POST_CART_INVALID_ITEMID(false, 2033, "잘못된 장바구니 담기 요청입니다."),
    INVALID_REVIEWID_ERROR(false, 2034, "조회할 수 없는 리뷰입니다."),
    DUPLICATED_REVIEW_LIKE(false, 2035, "이미 평가한 리뷰입니다."),
    INVALID_REVIEW_LIKE(false, 2036, "평가한 리뷰가 아닙니다."),

    PATCH_CART_INVALID_ITEMID(false, 2037, "잘못된 장바구니 수정 요청입니다."),
    DELETE_CART_INVALID_ITEMID(false, 2038, "잘못된 장바구니 삭제 요청입니다."),

    EMPTY_CART(false, 2039, "한 개 이상의 상품을 선택해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_EMAIL(false,4015,"이메일 수정 실패"),
    MODIFY_FAIL_PHONE(false,4016,"연락처 수정 실패"),
    MODIFY_FAIL_PASSWORD(false,4017,"패스워드 수정 실패"),
    MODIFY_FAIL_COMMERCIAL(false,4018,"마케팅동의 수정 실패"),
    MODIFY_FAIL_CSMS(false,4019,"SMS/SNS 수정 실패"),
    MODIFY_FAIL_CEMAIL(false,4020,"이메일 수신 수정 실패"),
    MODIFY_FAIL_APPPUSH(false,4021,"앱 푸시 수신 수정 실패"),
    MODIFY_FAIL_PROFILE(false,4022,"프로필 사진 수정 실패"),
    MODIFY_FAIL_DEL(false,4022,"회원 탈퇴 실패"),


    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
