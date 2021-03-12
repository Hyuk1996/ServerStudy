package com.example.demo.src.my;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.my.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/my")
public class MyController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MyProvider myProvider;
    @Autowired
    private final MyService myService;
    @Autowired
    private final JwtService jwtService;


    public MyController(MyProvider myProvider, MyService myService, JwtService jwtService){
        this.myProvider = myProvider;
        this.myService = myService;
        this.jwtService = jwtService;
    }

    /**
     * 관심 웹툰 조회 API
     * (GET) /my/:userIndex/interests
     * @return BaseResponse<GetMyInterestRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/interests") // (GET) https://www.hyukserver.site/app/my/:userIndex/interests
    public BaseResponse<GetMyInterestRes> getMyInterests(@PathVariable("userIndex") int userIndex){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        GetMyInterestRes getMyInterestRes = myProvider.getMyInterests(userIndex);
        return new BaseResponse<>(getMyInterestRes);
    }

    /**
     * 최근 본 웹툰 조회 API
     * (GET) /my/:userIndex/recent-webtoons
     * @return BaseResponse<GetMyRecentWebtoonRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/recent-webtoons") // (GET) https://www.hyukserver.site/app/my/:userIndex/recent-webtoons
    public BaseResponse<GetMyRecentWebtoonRes> getMyRecentWebtoons(@PathVariable("userIndex") int userIndex){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        GetMyRecentWebtoonRes getMyRecentWebtoonRes = myProvider.getMyRecentWebtoons(userIndex);
        return new BaseResponse<>(getMyRecentWebtoonRes);
    }

    /**
     * 임시 저장 웹툰 조회 API
     * (GET) /my/:userIndex/temp-webtoons
     * @return BaseResponse<GetMyTemWebtoonRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/temp-webtoons") // (GET) https://www.hyukserver.site/app/my/:userIndex/temp-webtoons
    public BaseResponse<GetMyTemWebtoonRes> getMyTemWebtoons(@PathVariable("userIndex") int userIndex){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        GetMyTemWebtoonRes getMyTemWebtoonRes = myProvider.getMyTemWebtoons(userIndex);
        return new BaseResponse<>(getMyTemWebtoonRes);
    }

    /** 구입 쿠키내역 조회 API
     * (GET) /my/:userIndex/bought-cookies
     * @return BaseResponse<GetMyCookieBuyRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/bought-cookies") // (GET) https://www.hyukserver.site/app/my/:userIndex/bought-cookies
    public BaseResponse<GetMyCookieBuyRes> getMyCookieBuy(@PathVariable("userIndex") int userIndex){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        GetMyCookieBuyRes getMyCookieBuyRes = myProvider.getMyCookieBuy(userIndex);
        return new BaseResponse<>(getMyCookieBuyRes);
    }

    /** 사용 쿠키 내역 조회 API
     * (GET) /my/:userIndex/used-cookies
     * @return BaseResponse<GetMyCookieUseRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/used-cookies") // (GET) https://www.hyukserver.site/app/my/:userIndex/used-cookies
    public BaseResponse<GetMyCookieUseRes> getMyCookieUse(@PathVariable("userIndex") int userIndex){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        GetMyCookieUseRes getMyCookieUseRes = myProvider.getMyCookieUse(userIndex);
        return new BaseResponse<>(getMyCookieUseRes);
    }

    /** 관심 웹툰 추가 API
     * (POST) /my/:userIndex/interest
     * @return BaseResponse<PostMyInterestRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/interest") // (POST) https://www.hyukserver.site/app/my/:userIndex/interest
    public BaseResponse<PostMyInterestRes> postMyInterest(@PathVariable("userIndex") int userIndex, @RequestBody PostMyInterestReq postMyInterestReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postMyInterestReq.getWebtoonIndex() == null){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        try {
            PostMyInterestRes postMyInterestRes = myService.postMyInterest(userIndex, postMyInterestReq.getWebtoonIndex());
            return new BaseResponse<>(postMyInterestRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 관심 웹툰 삭제 API
     * (PATCH) /my/:userIndex/interest/status
     * @return BaseResponse<PatchMyInterestRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/interest/status") // (PATCH) https://www.hyukserver.site/app/my/:userIndex/interest/status
    public BaseResponse<PatchMyInterestRes> patchMyInterest(@PathVariable("userIndex") int userIndex, @RequestBody PatchMyInterestReq patchMyInterestReq){
        // validation
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchMyInterestReq.getWebtoonIndex() == null){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        try{
            PatchMyInterestRes patchMyInterestRes = myService.patchMyInterest(userIndex,patchMyInterestReq.getWebtoonIndex());
            return new BaseResponse<>(patchMyInterestRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 알람 켜기 API
     * (PATCH) /my/:userIndex/alarm-on
     * @return BaseResponse<PatchMyAlarmRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/alarm-on") // (PATCH) https://www.hyukserver.site/app/my/:userIndex/alarm-on
    public BaseResponse<PatchMyAlarmRes> patchMyAlarmOn(@PathVariable("userIndex") int userIndex, @RequestBody PatchMyAlarmReq patchMyAlarmReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchMyAlarmReq.getWebtoonIndex() == null){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        PatchMyAlarmRes patchMyAlarmRes = myService.patchMyAlarmOn(userIndex, patchMyAlarmReq.getWebtoonIndex());
        return new BaseResponse<>(patchMyAlarmRes);
    }

    /**
     * 알람 끄기 API
     * (PATCH) /my/:userIndex/alarm-off
     * @return BaseResponse<patchMyAlarmRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/alarm-off") // (PATCH) https://www.hyukserver.site/app/my/:userIndex/alarm-off
    public BaseResponse<PatchMyAlarmRes> patchMyAlarmOff(@PathVariable("userIndex") int userIndex, @RequestBody PatchMyAlarmReq patchMyAlarmReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchMyAlarmReq.getWebtoonIndex() == null){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        PatchMyAlarmRes patchMyAlarmRes = myService.patchMyAlarmOff(userIndex, patchMyAlarmReq.getWebtoonIndex());
        return new BaseResponse<>(patchMyAlarmRes);
    }

    /**
     * 읽은 기록 추가 API
     * (POST) /my/:userIndex/log
     * @return BaseResponse<PostMyLogRes>
     */
    @ResponseBody
    @PostMapping("{userIndex}/log") // (POST) https://www.hyukserver.site/app/my/:userIndex/log
    public BaseResponse<PostMyLogRes> postMyLog(@PathVariable("userIndex") int userIndex, @RequestBody PostMyLogReq postMyLogReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postMyLogReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }

        try {
            PostMyLogRes postMyLogRes = myService.postMyLog(userIndex, postMyLogReq.getContentIndex());
            return new BaseResponse<>(postMyLogRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 임시 저장 콘텐츠 추가 API
     * (POST) /my/:userIndex/temp-content
     * @return BaseResponse<PostMyTempContentRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/temp-content") // (POST) https://www.hyukserver.site/app/my/:userIndex/temp-content
    public BaseResponse<PostMyTempContentRes> postMyTempContent(@PathVariable("userIndex") int userIndex, @RequestBody PostMyTempContentReq postMyTempContentReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postMyTempContentReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }

        try {
            PostMyTempContentRes postMyTempContentRes = myService.postMyTempContent(userIndex, postMyTempContentReq.getContentIndex());
            return new BaseResponse<>(postMyTempContentRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 임시 저장 컨텐츠 삭제 API
     * (PATCH) /my/:userIndex/temp-content/status
     * @return BaseResponse<PatchMyTempContentRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/temp-content/status") // (PATCH) https://www.hyukserver.site/app/my/:userIndex/temp-content/status
    public BaseResponse<PatchMyTempContentRes> patchMyTempContent(@PathVariable("userIndex") int userIndex, @RequestBody PatchMyTempContentReq patchMyTempContentReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchMyTempContentReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }

        try{
            PatchMyTempContentRes patchMyTempContentRes = myService.patchMyTempContent(userIndex, patchMyTempContentReq.getContentIndex());
            return new BaseResponse<>(patchMyTempContentRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 쿠키 구입 API
     * [POST] /my/:userIndex/cookie-buy
     * @return BaseResponse<PostMyCookieBuyRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/cookie-buy") // (POST) https://www.hyukserver.site/app/my/:userIndex/cookie-buy
    public BaseResponse<PostMyCookieBuyRes> postMyCookieBuy(@PathVariable("userIndex") int userIndex, @RequestBody PostMyCookieBuyReq postMyCookieBuyReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postMyCookieBuyReq.getCookie() == null){
            return new BaseResponse<>(EMPTY_COOKIE);
        }

        PostMyCookieBuyRes postMyCookieBuyRes = myService.postMyCookieBuy(userIndex, postMyCookieBuyReq.getCookie());
        return new BaseResponse<>(postMyCookieBuyRes);
    }

    /** 쿠키 사용 API
     * [POST] /my/:userIndex/cookie-use
     * @return BaseResponse<PostMyCookieUseRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/cookie-use") // (POST) https://www.hyukserver.site/app/my/:userIndex/cookie-use
    public BaseResponse<PostMyCookieUseRes> postMyCookieUse(@PathVariable("userIndex") int userIndex, @RequestBody PostMyCookieUseReq postMyCookieUseReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postMyCookieUseReq.getCookie() == null){
            return new BaseResponse<>(EMPTY_COOKIE);
        }
        if(postMyCookieUseReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_CONTENTINDEX);
        }

        try{
            PostMyCookieUseRes postMyCookieUseRes = myService.postMyCookieUse(userIndex, postMyCookieUseReq);
            return new BaseResponse<>(postMyCookieUseRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
