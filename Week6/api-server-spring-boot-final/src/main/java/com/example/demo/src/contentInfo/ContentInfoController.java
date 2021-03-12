package com.example.demo.src.contentInfo;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.contentInfo.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/naver-webtoon/content-info")
public class ContentInfoController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ContentInfoProvider contentInfoProvider;
    @Autowired
    private final ContentInfoService contentInfoService;
    @Autowired
    private final JwtService jwtService;


    public ContentInfoController(ContentInfoProvider contentInfoProvider, ContentInfoService contentInfoService, JwtService jwtService){
        this.contentInfoProvider = contentInfoProvider;
        this.contentInfoService = contentInfoService;
        this.jwtService = jwtService;
    }

    /** 웹툰 내용 조회 API
     * (GET) /content-info/:contentIndex
     * @return BaseResponse<GetContentInfoRes>
     */
    //Path Variable
    @ResponseBody
    @GetMapping("/{contentIndex}") // (GET) https://www.hyukserver.site/naver-webtoon/content-info/:contentIndex
    public BaseResponse<GetContentInfoRes> getContentInfo(@PathVariable("contentIndex") int contentIndex){
        GetContentInfoRes getContentInfoRes = contentInfoProvider.getContentInfo(contentIndex);
        return new BaseResponse<>(getContentInfoRes);
    }
}
