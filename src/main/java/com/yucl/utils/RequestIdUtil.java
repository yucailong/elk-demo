package com.yucl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author yucl
 * @date 2018/9/13
 */
public class RequestIdUtil {


    public static final String REQUEST_ID_KEY = "requestId";
    public static ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<String>();

    private static final Logger logger = LoggerFactory.getLogger(RequestIdUtil.class);

    public static String getRequestId(HttpServletRequest request) {
        String requestId = null;
        String parameterRequestId = request.getParameter(REQUEST_ID_KEY);
        String headerRequestId = request.getHeader(REQUEST_ID_KEY);

        if (parameterRequestId == null && headerRequestId == null) {
            logger.info("request parameter 和header 都没有requestId入参");
            requestId = UUID.randomUUID().toString();
        } else {
            requestId = parameterRequestId != null ? parameterRequestId : headerRequestId;
        }

        requestIdThreadLocal.set(requestId);

        return requestId;
    }
}
