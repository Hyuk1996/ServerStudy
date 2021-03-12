package com.example.demo.src.contentList;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.contentList.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/naver-webtoon/content-list")
public class ContentListController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ContentListProvider contentListProvider;
    @Autowired
    private final ContentListService contentListService;
    @Autowired
    private final JwtService jwtService;


    public ContentListController(ContentListProvider contentListProvider, ContentListService contentListService, JwtService jwtService){
        this.contentListProvider = contentListProvider;
        this.contentListService = contentListService;
        this.jwtService = jwtService;
    }

    /**
     * 웹툰 회차에서 웹툰 정보 조회 API
     * [GET] /content-list/:webtoonIndex/webtoon-info
     * @return BaseResponse<GetWebtoonInfoRes>
     */
    //Path Variable
    @ResponseBody
    @GetMapping("/{webtoonIndex}/webtoon-info") // (GET) https://www.hyukserver.site/naver-webtoon/content-list/:webtoonIndex/webtoon-info
    public BaseResponse<GetWebtoonInfoRes> getWebtoonInfo(@PathVariable("webtoonIndex") int webtoonIndex){
        GetWebtoonInfoRes getWebtoonInfoRes = contentListProvider.getWebtoonInfo(webtoonIndex);
        return new BaseResponse<>(getWebtoonInfoRes);
    }

    /** 웹툰 회차 리스트 조회 API
     * [GET] /content-list/:webtoonIndex
     * @return BaseResponse<List<GetContentListRes>>
     */
    //Path Vairable
    @ResponseBody
    @GetMapping("/{webtoonIndex}") // (GET) https://www.hyukserver.site/naver-webtoon/content-list/:webtoonIndex
    public BaseResponse<List<GetContentListRes>> getContentList(@PathVariable("webtoonIndex") int webtoonIndex){
        List<GetContentListRes> getContentListRes = contentListProvider.getContentList(webtoonIndex);
        return new BaseResponse<>(getContentListRes);
    }

}
