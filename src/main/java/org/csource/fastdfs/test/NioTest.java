package org.csource.fastdfs.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.csource.fastdfs.NioHander1;
import org.csource.fastdfs.NioHandler;
import org.csource.fastdfs.ProtoCommon;

/**
 * Created by he.wc on 2014/12/10.
 */
public class NioTest {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap  bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        final DefaultRequest request = new DefaultRequest();
        ResponseFuture responseFuture = new ResponseFuture(request);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("handler", new NioHander1(request.getmId()))
                        .addLast("decoder", new ByteArrayDecoder())
                        .addLast("encoder", new ByteArrayEncoder());
            }
        });
        bootstrap.connect("10.0.30.166",22122).channel().closeFuture().sync();

        System.out.println("response = "+responseFuture.get());


        eventLoopGroup.shutdownGracefully();
    }
}
