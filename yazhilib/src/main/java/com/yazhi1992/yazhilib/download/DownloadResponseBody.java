package com.yazhi1992.yazhilib.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zengyazhi on 17/7/25.
 * <p>
 * 更新下载进度的自定义 ResponseBody
 */

public class DownloadResponseBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private DownloadProgressListener mProgressListener;
    private BufferedSource mBufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, DownloadProgressListener progressListener) {
        mResponseBody = responseBody;
        mProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null != mProgressListener) {
                    mProgressListener.update(totalBytesRead, mResponseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };

    }
}
