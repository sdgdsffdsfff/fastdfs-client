package org.csource.fastdfs.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by he.wc on 2014/12/16.
 */
public class ResponseFuture {

    private static final ConcurrentHashMap<Long,ResponseFuture> futures = new ConcurrentHashMap<Long, ResponseFuture>();

    private final Lock lock = new ReentrantLock();

    private Condition done = lock.newCondition();

    private String reslut;

    public ResponseFuture(DefaultRequest request){
        futures.put(request.getmId(),this);
    }

    public String get(){
        lock.lock();
        try{
            while (!isDone()){
                done.await();
            }
        }catch (Exception e){
           throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
        return reslut;
    }

    public static void received(String reslut, long id){
        ResponseFuture future = futures.remove(id);
        if(future != null){
            future.doReceived(reslut);
        }
    }

    private void doReceived(String reslut){
        lock.lock();
        try {
            this.reslut = reslut;
            done.signal();
        }finally {
            lock.unlock();
        }
    }

    private boolean isDone(){
        return  this.reslut != null;
    }

}
