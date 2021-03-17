package com.ivory.isasa.util;

import org.apache.catalina.Context;
import org.apache.catalina.util.ParameterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestUtil {

    private final static Logger logger = LoggerFactory.getLogger(com.ivory.isasa.util.RequestUtil.class);

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static RequestBody getRequestBody(String json) {
        return RequestBody.create(JSON, json);
    }


    public static String getPostAdd(String url) {
        return url + "?";
    }


    public static String requestJsonPost(String url, RequestBody requestBody) throws IOException {
        Response response = new OkHttpClient().newCall(new Request.Builder().url(getPostAdd(url)
                ).post(
                requestBody).build()
        ).execute();
        if (response.code() == 200) {
            return response.body().string();
        }
        return null;
    }

    /**
     * describe: 正式Get
     *
     * @Author: Aaron
     * @Date: 2020/10/18 16:08
     */
    public static String requestGet(String url, ParameterMap parameterMap){
        url = url + "?" ;
        if (null != parameterMap && parameterMap.size() > 0) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                url = url + "&" + key + "=" + parameterMap.get(key);
            }
        }
        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(url).get().build()).execute();
            return response.body().string();
        } catch (IOException e) {
            System.out.println("e = " + e);
            return null;
        }
    }

    /**
     * describe: 为了使用本地服务器测试的Test
     *
     * @Author: Aaron
     * @Date: 2020/10/18 16:08
     */
//    public static String requestGet(String url, ParameterMap parameterMap){
//
//        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
//
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                //强行返回true 即验证成功
//                return true;
//            }
//        }).build();
//
//        url = url + "?";
//        if (null != parameterMap && parameterMap.size() > 0) {
//            Iterator<String> iterator = parameterMap.keySet().iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                url = url + "&" + key + "=" + parameterMap.get(key);
//            }
//        }
//        try {
//            Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
//            return response.body().string();
//        } catch (IOException e) {
//            System.out.println("e = " + e);
//            return null;
//        }
//    }

    /**
     * describe: 正式PUT
     *
     * @Author: Aaron
     * @Date: 2020/10/18 16:08
     */
    public static String requestPut(String url){
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "test:test");
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Connection", "keep-alive")
                .build();


        try {
            Response response = new OkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            System.out.println("e = " + e);
           return null;
        }
    }

    /**
     * describe: 为了使用本地服务器测试的PutTest
     *
     * @Param: url
     * @Return: 结果
     * @Author: Aaron
     * @Date: 2020/10/18 16:06
     */
//    public static String requestPut(String url){
//        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                //强行返回true 即验证成功
//                return true;
//            }
//        }).build();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "test:test");
//        Request request = new Request.Builder()
//                .url(url)
//                .put(body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Connection", "keep-alive")
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        } catch (IOException e) {
//            System.out.println("e = " + e);
//            return null;
//        }
//    }

}
