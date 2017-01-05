/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.amiapi;

import com.johannes.lsctic.FXMLController;
import com.johannes.lsctic.fields.InternField;
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class ServerConnectionHandler {
     static final String ADRESSE = "localhost";
     static final int PORT = 12345;
    
    private Channel ch;
    private final Map<String, InternField> internNumbers;
    private final FXMLController fcont;
    public ServerConnectionHandler(Map<String, InternField> internNumbers, FXMLController fcont) throws IOException {
        this.internNumbers = internNumbers;
             this.fcont = fcont;
         try {
             final SslContext sslCtx = SslContextBuilder.forClient()
                     .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
             
             EventLoopGroup group = new NioEventLoopGroup();
             
             Bootstrap b = new Bootstrap();
             b.group(group)
                     .channel(NioSocketChannel.class)
                     .handler(new SecureChatClientInitializer(sslCtx,  fcont, internNumbers));
             
             // Start the connection attempt.
             ch = b.connect(ADRESSE, PORT).sync().channel();
             
             // Read commands from the stdin.
             
             
             // If user typed the 'bye' command, wait until the server closes
             // the connection.
             
             // Wait until all messages are flushed before closing the channel.
             
             
             
         } catch (InterruptedException ex) {
             Logger.getLogger(ServerConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
         
    }
    public void sendBack(String msg) {
         ChannelFuture lastWriteFuture = ch.writeAndFlush(msg + "\r\n");
    }

    public void aboStatusExtension(String phoneNumber) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush("000" + phoneNumber + "\r\n");
    }

    public void deAboStatusExtension(String phoneNumber) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush("001" + phoneNumber + "\r\n");
    }
    
    public void aboCdrExtension(String phoneNumber) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush("004" + phoneNumber + "\r\n");
    }
    public void call(String phoneNumber) {
        ChannelFuture lastWriteFuture = ch.writeAndFlush("003" + phoneNumber + "\r\n");
   }
    
}
