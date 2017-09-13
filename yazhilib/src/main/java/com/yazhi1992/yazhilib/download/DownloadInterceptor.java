package com.yazhi1992.yazhilib.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 拦截器，对response进行拦截以增加监听
 */
public class DownloadInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public DownloadInterceptor(DownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
