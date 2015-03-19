package org.csource.fastdfs.test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by he.wc on 2014/12/16.
 */
public class DefaultRequest {

    private static final AtomicLong INVOKE_ID =new AtomicLong(0);

    private final long mId;

    public DefaultRequest(){
        mId = newId();
    }

    private static long newId(){
        return INVOKE_ID.getAndIncrement();
    }

    public long getmId() {
        return mId;
    }
}
