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
    INVALID_WEBTOON_IDX(false, 2003, "존재하지 않는 웹툰 인덱스입니다."),
    EMPTY_WEBTOONINDEX(false, 2004, "웹툰 인덱스를 입력해주세요."),
    EMPTY_CONTENTINDEX(false, 2005, "컨텐츠 인덱스를 입력해주세요."),
    EMPTY_GRADE(false, 2006, "별점을 입력하세요."),
    EMPTY_COMMENT_CONTENT(false, 2007, "댓글을 입력하세요."),
    EMPTY_COMMENTINDEX(false, 2008, "댓글 인덱스를 입력하세요."),
    EMPTY_USERINDEX(false, 2009, "유저 인덱스를 입력하세요."),
    EMPTY_COOKIE(false, 2050, "쿠키값을 입력하세요."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2020, "비밀번호를 입력해주세요."),

    // [GET] /users
    GET_USERS_INVALID_IDX(false, 2011, "존재하지 않는 인덱스입니다."),
    GET_USERS_INVALID_USER(false, 2012, "존재하지 않는 회원입니다."),

    // [POST] /users
    POST_USERS_EMPTY_USER_ID(false, 2015, "유저 아이디을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_USER_ID(false,2017,"중복된 아이디입니다."),
    POST_USERS_EMPTY_NICKNAME(false, 2019, "닉네임을 입력해주세요."),
    POST_USERS_EMPTY_GENDER(false, 2021, "성별을 입력해주세요."),
    POST_USERS_EXISTS_NICKNAME(false, 2022, "중복된 닉네임입니다."),

    // [PATCH] /users
    PATCH_USERS_EMPTY_STATUS(false, 2023, "상태값을 입력해주세요."),
    PATCH_USERS_INVALID_STATUS(false, 2024, "유효하지 않은 상태값입니다."),

    // [GET] /webtoons/day
    GET_WEBTOON_DAY_EMPTY(false, 2018, "연재일을 입력해주세요."),
    GET_WEBTOON_INVALID_DAY(false, 2025, "연재일 형식이 틀렸습니다."),

    // [POST] /my/log
    POST_LOG_CONTENT_EXISTS(false, 2100, "이미 읽은 콘텐츠입니다."),

    // [POST] /my/temp-content
    POST_TEMP_CONTENT_EXISTS(false, 2200, "이미 저장된 콘텐츠입니다."),

    // [PATCH] /my/temp-content/status
    PATCH_TEMP_CONTENT_INVALID(false, 2250, "저장 목록에 없는 콘텐츠입니다."),

    // [POST] /my/cookie-use
    POST_NO_COOKIE(false, 2251, "쿠키가 부족합니다."),

    // [POST] /webtoons/content/star
    POST_STAR_EXISTS(false, 2300, "이미 별점을 주었습니다."),

    // [POST] /webtoons/content/heart
    POST_HEART_EXISTS(false, 2350, "이미 하트를 주었습니다."),
    POST_HEART_INVALID(false, 2351, "하트가 유효하지 않습니다."),

    // [POST] /comments
    POST_COMMENT_EXISTS(false, 2400, "댓글을 이미 작성했습니다."),

    // [PATCH] /comments/status
    PATCH_COMMENT_INVALID(false, 2420, "존재하지 않는 댓글입니다."),

    // [POST] /comments/like
    POST_LIKE_OR_UNLIKE_EXISTS(false, 2500, "이미 누르셨습니다."),
    POST_LIKE_INVALID(false, 2501, "좋아요 누르시지 않았습니다."),

    // [POST] /comments/unlike
    POST_UNLIKE_INVALID(false, 2551, "싫어요 누르지 않았습니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

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
