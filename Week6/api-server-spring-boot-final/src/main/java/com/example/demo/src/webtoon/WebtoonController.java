package com.example.demo.src.webtoon;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.webtoon.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/webtoons")
public class WebtoonController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final WebtoonProvider webtoonProvider;
    @Autowired
    private final WebtoonService webtoonService;
    @Autowired
    private final JwtService jwtService;


    public WebtoonController(WebtoonProvider webtoonProvider, WebtoonService webtoonService, JwtService jwtService){
        this.webtoonProvider = webtoonProvider;
        this.webtoonService = webtoonService;
        this.jwtService = jwtService;
    }


    /**
     * 요일 별 웹툰 조회 API
     * [GET] /webtoons/day=?serialDay=?
     * @return BaseResponse<List<GetWebtoonRes>>
     */
    @ResponseBody
    @GetMapping("/day") // (GET) https://www.hyukserver.site/app/webtoons/day?serialDate=?
    public BaseResponse<List<GetWebtoonRes>> getWebtoonsDay(@RequestParam(value="serialDay", required=false, defaultValue = "0") int serialDay){
        if(serialDay == 0){
            return new BaseResponse<>(GET_WEBTOON_DAY_EMPTY);
        }
        String serialDate;
        switch(serialDay){
            case 1:
                serialDate = "월요웹툰";
                break;
            case 2:
                serialDate = "화요웹툰";
                break;
            case 3:
                serialDate = "수요웹툰";
                break;
            case 4:
                serialDate = "목요웹툰";
                break;
            case 5:
                serialDate = "금요웹툰";
                break;
            case 6:
                serialDate = "토요웹툰";
                break;
            case 7:
                serialDate = "일요웹툰";
                break;
            default:
                return new BaseResponse<>(GET_WEBTOON_INVALID_DAY);
        }

        List<GetWebtoonRes> getWebtoonsRes = webtoonProvider.getWebtoonsDay(serialDate);
        return new BaseResponse<>(getWebtoonsRes);
    }

    /**
     * 신작 웹툰 조회 API
     * [GET] /webtoons/new
     * @return BaseResponse<List<GetWebtoonNewRes>>
     */
    @ResponseBody
    @GetMapping("/new") // (GET) https://www.hyukserver.site/app/webtoons/new
    public BaseResponse<List<GetWebtoonNewRes>> getWebtoonsNew(){
        List<GetWebtoonNewRes> getWebtoonsNewRes = webtoonProvider.getWebtoonsNew();
        return new BaseResponse<>(getWebtoonsNewRes);
    }

    /**
     * 완결 웹툰 조회 API
     * [GET] /webtoons/finish
     * @return BaseResponse<List<GetWebtoonFinRes>>
     */
    @ResponseBody
    @GetMapping("/finish") // (GET) https://www.hyukserver.site/app/webtoons/finish
    public BaseResponse<List<GetWebtoonFinRes>> getWebtoonsFinish(){
        List<GetWebtoonFinRes> getWebtoonsFinRes = webtoonProvider.getWebtoonsFinish();
        return new BaseResponse<>(getWebtoonsFinRes);
    }

    /** 웹툰 콘텐츠 조회 API
     * [GET] /webtoons/:userIndex/contents?webtoonIndex=
     * @return BaseResponse<GetWebtoonContentsRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/contents") // (GET) https://www.hyukserver.site/app/webtoons/:userIndex/contents
    public BaseResponse<GetWebtoonContentsRes> getWebtoonContents(@PathVariable("userIndex") int userIndex, @RequestParam(value="webtoonIndex", required = false, defaultValue="0") int webtoonIndex){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(webtoonIndex == 0){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        GetWebtoonContentsRes getWebtoonContentsRes = webtoonProvider.getWebtoonContents(userIndex, webtoonIndex);
        return new BaseResponse<>(getWebtoonContentsRes);
    }

    /**
     * 별점 주기 API
     * [POST] /webtoons/:userIndex/content/star
     * @return BaseResponse<PostWebtoonStarRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/content/star") // (POST) https://www.hyukserver.site/app/webtoons/:userIndex/content/star
    public BaseResponse<PostWebtoonStarRes> postWebtoonStar(@PathVariable("userIndex") int userIndex, @RequestBody PostWebtoonStarReq postWebtoonStarReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        if(postWebtoonStarReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }
        if(postWebtoonStarReq.getGrade() == null){
            return new BaseResponse<>(EMPTY_GRADE);
        }

        try{
            PostWebtoonStarRes postWebtoonStarRes = webtoonService.postWebtoonStar(userIndex, postWebtoonStarReq);
            return new BaseResponse<>(postWebtoonStarRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 하트 추가 API
     * [POST] /webtoons/:userIndex/content/heart
     * @return BaseResponse<PostWebtoonHeartRes>
     */
    @ResponseBody
    @PostMapping("/{userIndex}/content/heart") // (POST) https://www.hyukserver.site/app/webtoons/:userIndex/content/heart
    public BaseResponse<PostWebtoonHeartRes> postWebtoonHeart(@PathVariable("userIndex") int userIndex, @RequestBody PostWebtoonHeartReq postWebtoonHeartReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(postWebtoonHeartReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        try{
            PostWebtoonHeartRes postWebtoonHeartRes = webtoonService.postWebtoonHeart(userIndex, postWebtoonHeartReq.getContentIndex());
            return new BaseResponse<>(postWebtoonHeartRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 하트 삭제 API
     * [PATCH] /webtoons/:userIndex/content/heart/status
     * @return BaseResponse<PatchWebtoonHeartRes>
     */
    @ResponseBody
    @PatchMapping("/{userIndex}/content/heart/status") // (PATCH) https://www.hyukserver.site/app/webtoons/:userIndex/content/heart/status
    public BaseResponse<PatchWebtoonHeartRes> patchWebtoonHeart(@PathVariable("userIndex") int userIndex, @RequestBody PatchWebtoonHeartReq patchWebtoonHeartReq){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(patchWebtoonHeartReq.getContentIndex() == null){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        try{
            PatchWebtoonHeartRes patchWebtoonHeartRes = webtoonService.patchWebtoonHeart(userIndex, patchWebtoonHeartReq.getContentIndex());
            return new BaseResponse<>(patchWebtoonHeartRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 콘텐츠 내용 조회 API
     * [GET] /webtoons/:userIndex/content-info?contentIndex=?
     * @return BaseResponse<GetWebtoonContentInfoRes>
     */
    @ResponseBody
    @GetMapping("/{userIndex}/content-info") // (GET) https://www.hyukserver.site/app/webtoons/:userIndex/content-info?contentIndex=?
    public BaseResponse<GetWebtoonContentInfoRes> getWebtoonContentInfo(@PathVariable("userIndex") int userIndex, @RequestParam(value="contentIndex", required=false, defaultValue="0") int contentIndex){
        try {
            if (userIndex != jwtService.getUserIdx()) {
                return new BaseResponse<>(INVALID_JWT);
            }
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        if(contentIndex == 0){
            return new BaseResponse<>(EMPTY_WEBTOONINDEX);
        }

        GetWebtoonContentInfoRes getWebtoonContentInfoRes = webtoonProvider.getWebtoonContentInfo(userIndex, contentIndex);
        return new BaseResponse<>(getWebtoonContentInfoRes);
    }

}
