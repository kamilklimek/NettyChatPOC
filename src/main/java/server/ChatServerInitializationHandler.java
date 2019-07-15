package server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServerInitializationHandler extends SimpleChannelInboundHandler<String> {
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("Handler added for: " + channelHandlerContext.channel().localAddress());

        channelHandlerContext.pipeline().addLast("frame", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        channelHandlerContext.pipeline().addLast("decoder", new StringDecoder());
        channelHandlerContext.pipeline().addLast("encoder", new StringEncoder());
        channelHandlerContext.pipeline().addLast("resend", new ChatServerResendHandler());
    }

    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.printf("Handler removed");
    }

    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        System.out.println("Caught exception");
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }
}
