package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;

public class ChatServerResendHandler extends ChannelInboundHandlerAdapter {
    private final static ChannelGroup channelGroups = new DefaultChannelGroup("default", GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();

        String s = (String) msg;
        System.out.println(channel.localAddress() + " said: " + s);
    }

    private void notifyAllChatMembers(SocketAddress localAddress) throws InterruptedException {
        System.out.println("[LOG]: Notify all members of chat about joined new user.");
        channelGroups.write(localAddress + " has connected to chat.")
                .addListener(new ChannelGroupFutureListener() {
                    public void operationComplete(ChannelGroupFuture channelFutures) throws Exception {
                        System.out.println("Notifying operation all members is done." + channelFutures.isPartialFailure() + channelFutures.isCancelled() + channelFutures.isSuccess());
                    }
                });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[LOG]: User connected to ChatServerResendHandler");
        Channel channel = ctx.channel();

        channelGroups.add(channel);
        notifyAllChatMembers(channel.localAddress());
    }
}
