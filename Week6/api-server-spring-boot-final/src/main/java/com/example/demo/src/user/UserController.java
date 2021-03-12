package com.example.demo.src.user;

import com.fasterxml.jackson.databind.ser.Serializers;
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
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
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
     * 사용자의 남은 쿠키 정보 조회 API
     * [GET] /users/:userIndex/cookie
     * @return BaseResponse<GetUserCookieRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/cookie") // (GET) https://www.hyukserver.site/app/users/:userIndex/cookie
    public BaseResponse<GetUserCookieRes> getUserCookie(@PathVariable("userIndex") int userIndex){
        try{
            // 형식적 validation
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }

            // 사용자의 쿠키 조회
            GetUserCookieRes getUserCookieRes = userProvider.getUserCookie(userIndex);
            return new BaseResponse<>(getUserCookieRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users/join
     * @return BaseResponse<PostUserJoinRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/join") // (POST) https://www.hyukserver.site/app/users/join
    public BaseResponse<PostUserJoinRes> postUserJoin(@RequestBody PostUserJoinReq postUserJoinReq) {
        // 형식적 validation
        if(postUserJoinReq.getUserId() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_USER_ID);
        }
        if(postUserJoinReq.getNickname() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }
        if(postUserJoinReq.getUserPw() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if(postUserJoinReq.getGender() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_GENDER);
        }

        //이메일 정규표현
       /* if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }*/
        try{
            PostUserJoinRes postUserJoinRes = userService.postUserJoin(postUserJoinReq);
            return new BaseResponse<>(postUserJoinRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 로그인 API
     * [POST] /users/login
     * @return BaseResponse<PostUserLoginRes>
     */
    @ResponseBody
    @PostMapping("/login") // (POST) https://www.hyukserver.site/app/users/login
    public BaseResponse<PostUserLoginRes> postUserLogin(@RequestBody PostUserLoginReq postUserLoginReq){
        // 형식적 validation
        if(postUserLoginReq.getUserId() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_USER_ID);
        }
        if(postUserLoginReq.getUserPw() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        try{
            PostUserLoginRes postUserLoginRes = userProvider.postUserLogin(postUserLoginReq.getUserId(), postUserLoginReq.getUserPw());
            return new BaseResponse<>(postUserLoginRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 비빌번호 변경 API
     * [PATCH] /users/:userIndex/password
     * @return BaseReponse<PatchUserPwRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/password") // https://www.hyukserver.site/app/users/:userIndex/password
    public BaseResponse<PatchUserPwRes> patchUserPw(@PathVariable("userIndex") int userIndex, @RequestBody PatchUserPwReq patchUserPwReq){
        // 형식적 validation
        if(patchUserPwReq.getUserPw() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        PatchUserPwRes patchUserPwRes = userService.patchUserPw(userIndex, patchUserPwReq.getUserPw());
        return new BaseResponse<>(patchUserPwRes);
    }

    /** 회원 상태 변경 API
     * [PATCH] /users/:userIndex/status
     * return BaseResponse<PatchUserStatRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/status")  // (PATCH) https:www.hyukserver.site/app/users/:userIndex/status
    public BaseResponse<PatchUserStatRes> patchUserStat(@PathVariable("userIndex") int userIndex, @RequestBody PatchUserStatReq patchUserStatReq){
        //형식적 validation
        if(patchUserStatReq.getStatus() == null){
            return new BaseResponse<>(PATCH_USERS_EMPTY_STATUS);
        }
        else if(!patchUserStatReq.getStatus().equals("Y") && !patchUserStatReq.getStatus().equals("N")){
            return new BaseResponse<>(PATCH_USERS_INVALID_STATUS);
        }
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        PatchUserStatRes patchUserStatRes = userService.patchUserStat(userIndex, patchUserStatReq.getStatus());
        return new BaseResponse<>(patchUserStatRes);
    }


}
