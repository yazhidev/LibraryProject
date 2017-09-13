package com.yazhi1992.yazhilib.download;

/**
 * Created by zengyazhi on 2017/8/29.
 */

public abstract class DownInfo {
    /*下载url*/
    private String url;
    /*下载状态*/
    private DownloadState state;
    /*回调监听*/
    private HttpProgressOnNextListener listener;
    /*Retrofit创建时要用到的baseUrl*/
    private String baseUrl;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*下载唯一的HttpService*/
    private HttpService service;
    /*超时设置*/
    private  int DEFAULT_TIMEOUT = 6;
    /*存储位置*/
    private String savePath;

    public DownInfo(String url, String savePath) {
        this.url = url;
        this.savePath = savePath;
        setBaseUrl(getBasUrl(url));
    }

    public int getDEFAULT_TIMEOUT() {
        return DEFAULT_TIMEOUT;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public HttpService getService() {
        return service;
    }

    public void setService(HttpService service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        this.state = state;
    }

    public HttpProgressOnNextListener getListener() {
        return listener;
    }

    public void setListener(HttpProgressOnNextListener listener) {
        this.listener = listener;
    }

    /**
     * 读取baseurl
     * @param url
     * @return
     */
    protected String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }
}
