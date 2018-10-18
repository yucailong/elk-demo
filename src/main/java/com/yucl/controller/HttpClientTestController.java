package com.yucl.controller;

import com.yucl.bo.HttpClientTestBo;
import com.yucl.vo.HttpClientTestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author yucl
 * @date 2018/9/13
 */
@RequestMapping("/test")
@Controller
public class HttpClientTestController {

    @Autowired
    private HttpClientTestBo httpClientTestBo;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public HttpClientTestResult doPost(@RequestBody HttpClientTestResult result) {
        HttpClientTestResult testPost = httpClientTestBo.testPost(result);
        return testPost;
    }
}
