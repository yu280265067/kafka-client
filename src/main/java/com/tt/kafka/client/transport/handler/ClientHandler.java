package com.tt.kafka.client.transport.handler;

import com.tt.kafka.client.transport.protocol.Packet;
import com.tt.kafka.client.transport.Connection;
import com.tt.kafka.client.transport.NettyConnection;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Tboy
 */
@Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final MessageDispatcher dispatcher;

    public ClientHandler(MessageDispatcher dispatcher){
        this.dispatcher = dispatcher;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConnection.attachChannel(ctx.channel());
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connnection = NettyConnection.attachChannel(ctx.channel());
        connnection.close();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        dispatcher.dispatch(NettyConnection.attachChannel(ctx.channel()), (Packet)msg);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("exceptionCaught", cause);
    }
}
