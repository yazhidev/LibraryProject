package com.yazhi1992.yazhilib.utils;

import android.content.Context;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.android.AndroidSentryClientFactory;

/**
 * Created by zengyazhi on 17/7/7.
 */

public class LibSentryUtil {

    private LibSentryUtil() {
    }

    private static final String DSN = "http://798743a76d6d47938c53de9d140ca63c:c7858d9fe8ce4c66b71b805dcfab0948@101.37.173.206:9000/10";

    public static void init(Context context, String dsn) {
        Sentry.init(dsn, new AndroidSentryClientFactory(context));
    }

    /**
     * 上传到sentry
     *
     * @param str
     */
    public static void captureMessage(String str) {
        Sentry.capture(str);
    }

    /**
     * 配置用户信息
     *
     * @param phone
     * @param name
     * @param id
     */
    public static void setUserInfo(String phone, String name, int id) {
        SentryClient storedClient = Sentry.getStoredClient();
        storedClient.addTag("student_id", String.valueOf(id));
        storedClient.addTag("student_name", String.valueOf(name));
        storedClient.addTag("student_phone", String.valueOf(phone));
        Sentry.setStoredClient(storedClient);
    }
}
