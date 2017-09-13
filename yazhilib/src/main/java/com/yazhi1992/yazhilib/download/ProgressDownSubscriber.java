package com.yazhi1992.yazhilib.download;


import java.lang.ref.WeakReference;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class ProgressDownSubscriber<T> extends Subscriber<T> implements DownloadProgressListener {
    //弱引用结果回调
    private WeakReference<HttpProgressOnNextListener> mSubscriberOnNextListener;
    /*下载数据*/
    private DownInfo downInfo;


    public ProgressDownSubscriber(DownInfo downInfo) {
        this.mSubscriberOnNextListener = new WeakReference<>(downInfo.getListener());
        this.downInfo = downInfo;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onStart();
        }
        downInfo.setState(DownloadState.START);
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        HttpDownManager.getInstance().stopDown(downInfo);
        downInfo.setState(DownloadState.FINISH);
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onComplete();
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        /*停止下载*/
        HttpDownManager.getInstance().stopDown(downInfo);
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onError(e);
        }
        downInfo.setState(DownloadState.ERROR);
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }

    @Override
    public void update(long read, long count, boolean done) {
        if (downInfo.getCountLength() > count) {
            read = downInfo.getCountLength() - count + read;
        } else {
            downInfo.setCountLength(count);
        }
        downInfo.setReadLength(read);

        if (downInfo.getState() == DownloadState.PAUSE
                || downInfo.getState() == DownloadState.STOP) return;
        downInfo.setState(DownloadState.DOWN);
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().updateProgress(read, downInfo.getCountLength());
        }
    }

}