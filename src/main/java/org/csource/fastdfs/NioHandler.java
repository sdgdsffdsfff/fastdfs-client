package org.csource.fastdfs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by he.wc on 2014/12/10.
 */
public class NioHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf input) throws Exception {
       // byte[] header = (byte[])o;
        //long pkg_len = ProtoCommon.buff2long(header, 0);
       // System.out.println(pkg_len);
        System.out.println("object is "+input);
        int readable = input.readableBytes();
        byte[] bytes = new byte[readable];
        if(readable <= 0){
           return;
        }
        input.readBytes(bytes);
        System.out.println(new String(bytes));
        long pkg_len = ProtoCommon.buff2long(bytes, 0);
        String ipAddress = new String(bytes,26,15).trim();
        System.out.println(pkg_len);
        System.out.println(ipAddress);

        channelHandlerContext.close();

    }
}
