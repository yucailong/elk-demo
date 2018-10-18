package com.yucl.bo;

import com.google.gson.Gson;
import com.yucl.utils.HttpClientUtils;
import com.yucl.utils.RequestIdUtil;
import com.yucl.vo.HttpClientTestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yucl
 * @date 2018/9/13
 */
@Service
public class HttpClientTestBo {

    private static Logger logger = LoggerFactory.getLogger(HttpClientTestBo.class);

//    @Value("${test_http_client_url}")
//    private String testHttpClientUrl;

    public HttpClientTestResult testPost(HttpClientTestResult result) {
        logger.info(new Gson().toJson(result));
        result.setCount(result.getCount() + 1);
        if (result.getCount()>= 1) {
            Map<String, String> headerMap = new HashMap <String, String>();
            String requestId = RequestIdUtil.requestIdThreadLocal.get();
            headerMap.put(RequestIdUtil.REQUEST_ID_KEY, requestId);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("status", result.getStatus() + "");
            paramMap.put("errorCode", result.getErrorCode());
            paramMap.put("message", result.getMessage());
            paramMap.put("count", result.getCount() + "");
//            String resultString = HttpClientUtils.doPost(testHttpClientUrl, paramMap);
            logger.info(paramMap.toString());
        }

        logger.info(new Gson().toJson(result));
        return result;
    }
}
