package com.taskit.taskit_android.network;
import android.content.Context;

import com.loopj.android.http.*;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * encapsulated interface for network connecting
 * Created by nick on 2018/7/11.
 */
public class HttpClientUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "http://sandbox.tas-kit.com/api/v1/";

    private static OkHttpClient client = new OkHttpClient();

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postJson(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}




