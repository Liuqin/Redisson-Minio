package org.qin.request_more;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;


/**
 * @author LiuQin
 */


@Component
public class MultipleNodeRequest {


    private final Logger logger = LoggerFactory.getLogger(MultipleNodeRequest.class);


    /**
     * 根据map获取get请求参数
     *
     * @param queries
     * @return
     */
    public StringBuffer getQueryString(String url, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        return sb;
    }


    /**
     * 调用okhttp的newCall方法
     *
     * @param request
     * @return
     */
    private String execNewCall(Request request, boolean isCanceled) {
        if (isCanceled) {
            return null;
        }
        Response response = null;
        try {

            if (isCanceled) {
                return null;
            }
            response = new OkHttpClient().newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful() && !isCanceled) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", e.getStackTrace());

        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "";
    }


    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public String PostJson(MultipleNode.ThreadRequest threadRequest) {
        PostData postData = threadRequest.getPostData();
        if (postData != null) {
            String parms = postData.getParms();
            if (parms == null) {
                parms = "{}";
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parms);
            Request request = new Request.Builder().url(postData.getUrl()).post(requestBody).build();
            return execNewCall(request, threadRequest.isCanceled());
        } else {
            return null;
        }
    }
}