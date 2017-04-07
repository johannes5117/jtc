/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.amiapi.netty;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class ServerConnectionHandler {
     static final String ADDRESS = "localhost";
     static final int PORT = 12345;
     private final EventBus bus;
     private final String  ownExtension;
    
    private Channel ch;
    public ServerConnectionHandler(EventBus bus, String ownExtension) throws IOException {
        this.bus = bus;
        this.ownExtension = ownExtension;
        bus.register(this);
         try {
             final SslContext sslCtx = SslContextBuilder.forClient()
                     .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
             
             EventLoopGroup group = new NioEventLoopGroup();
             
             Bootstrap b = new Bootstrap();
             b.group(group)
                     .channel(NioSocketChannel.class)
                     .handler(new SecureChatClientInitializer(sslCtx,this.bus, this.ownExtension));

             // Start the connection attempt.
             ch = b.connect(ADDRESS, PORT).sync().channel();

         } catch (InterruptedException ex) {
             Logger.getLogger(ServerConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
         
    }
    @Subscribe
    public void sendBack(SendBackEvent event) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush(event.getMessage() + "\r\n");
    }

    @Subscribe
    public void aboStatusExtension(AboStatusExtensionEvent event) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush("000" + event.getPhonenumber() + "\r\n");
    }

    @Subscribe
    public void deAboStatusExtension(DeAboStatusExtension event) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush("001" + event.getPhonenumber() + "\r\n");
    }

    @Subscribe
    public void aboCdrExtension(AboCdrExtensionEvent event) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush("004" + event.getPhoneNumber() + "\r\n");
    }

    @Subscribe
    public void call(CallEvent event) {
        Logger.getLogger(getClass().getName()).info("Versucht: "+ event.getPhoneNumber()+ " anzurufen");
        ChannelFuture lastWriteFuture = ch.writeAndFlush("003" + event.getPhoneNumber() + "\r\n");
   }
    
}
