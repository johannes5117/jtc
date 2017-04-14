/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.amiapi.netty;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.johannes.lsctic.messagestage.ErrorMessage;
import com.johannes.lsctic.messagestage.SuccessMessage;
import com.johannes.lsctic.panels.gui.fields.otherevents.CloseApplicationSafelyEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.StartConnectionEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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
 * @author johannesengler
 */
public class ServerConnectionHandler {
    private boolean firstStart = true;
    private final EventBus bus;
    private String address;
    private int port;
    private final String ownExtension;
    private Channel ch;

    public ServerConnectionHandler(EventBus bus, String ownExtension) {
        this.bus = bus;
        this.ownExtension = ownExtension;
        bus.register(this);
    }
    @Subscribe
    public void closeConnection(CloseApplicationSafelyEvent event) {
        this.ch.disconnect();
        this.ch.close();
    }

    @Subscribe
    public void sendBack(SendBackEvent event) {
        this.write(event.getMessage() + "\r\n");
    }

    @Subscribe
    public void aboStatusExtension(AboStatusExtensionEvent event) {
        this.write("000" + event.getPhonenumber() + "\r\n");
    }

    @Subscribe
    public void deAboStatusExtension(DeAboStatusExtension event) {
        this.write("001" + event.getPhonenumber() + "\r\n");
    }

    @Subscribe
    public void aboCdrExtension(AboCdrExtensionEvent event) {
        this.write("004" + event.getPhoneNumber() + "\r\n");
    }

    @Subscribe
    public void call(CallEvent event) {
        Logger.getLogger(getClass().getName()).info("Versucht: " + event.getPhoneNumber() + " anzurufen");
        this.write("003" + ownExtension+":"+event.getPhoneNumber() + "\r\n");
    }

    @Subscribe
    public void startConnection(StartConnectionEvent event) {
        this.address = event.getAddress();
        this.port = event.getPort();
        try {
            final SslContext sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

            EventLoopGroup group = new NioEventLoopGroup();

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SecureChatClientInitializer(sslCtx, this.bus, this.ownExtension, this.address, this.port));

            // Start the connection attempt.
            ch = b.connect(address, port).sync().channel();

        } catch (InterruptedException | IOException | IllegalArgumentException ex) {
            Logger.getLogger(ServerConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            new ErrorMessage("Could not establish connection to server. Please edit connection to server under options.");
            firstStart = false;
        }
        if(ch!=null && ch.isOpen()) {
            aboCdrExtension(new AboCdrExtensionEvent(ownExtension));
            // It would be annoying if every time you start the program a message pops up that informs about the connection
            if(!firstStart) {
                new SuccessMessage("Established connection to server: "+ address);
            }
            firstStart = false;
        }
    }

    public void write(String message) {
        if(ch!=null && ch.isOpen()) {
            ch.writeAndFlush(message);
        }
    }

}
