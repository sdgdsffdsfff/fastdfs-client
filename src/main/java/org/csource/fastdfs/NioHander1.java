package org.csource.fastdfs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.csource.fastdfs.test.DefaultRequest;
import org.csource.fastdfs.test.ResponseFuture;

/**
 * Created by he.wc on 2014/12/11.
 */
public class NioHander1 extends ChannelInboundHandlerAdapter {

    private String ip;

    private long id;

    public NioHander1(long id){
        this.id = id;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf input = (ByteBuf)msg;
        int readable = input.readableBytes();
        byte[] bytes = new byte[readable];
        if(readable <= 0){
           return;
        }
        input.readBytes(bytes);
        long pkg_len = ProtoCommon.buff2long(bytes, 0);
        String ipAddress = new String(bytes,26,15).trim();
        ip = ipAddress;
        ResponseFuture.received(ip,id);

        input.release();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer(10);
        final byte cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
        //header = ProtoCommon.packHeader(cmd, 0, (byte) 0);
        byte[] header = new byte[10];
        header[8]=cmd;
        header[9]=(byte)0;
        byteBuf.writeBytes(header);
        ctx.write(byteBuf);
        ctx.flush();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
