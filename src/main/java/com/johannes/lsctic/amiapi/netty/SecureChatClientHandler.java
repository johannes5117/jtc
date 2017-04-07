/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.amiapi.netty;

/**
 * @author Johannes
 */

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.panels.gui.fields.AddCdrAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.SetStatusEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a client-side channel.
 */
public class SecureChatClientHandler extends SimpleChannelInboundHandler<String> {
    private EventBus bus;
    private String ownExtension;

    public SecureChatClientHandler(EventBus bus, String ownExtension) {
        this.bus = bus;
        this.ownExtension = ownExtension;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        try {
            String chatInput = msg;
            int op = Integer.parseInt(chatInput.substring(0, 3));
            String param = chatInput.substring(3, chatInput.length());
            switch (op) {
                case 0: {
                    String[] d = param.split(":");
                    String intern = d[0];
                    int state = Integer.parseInt(d[1]);
                    bus.post(new SetStatusEvent(state, intern));
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "New State");
                    break;
                }
                case 10: {
                    String[] d = param.split(":");
                    String source = d[0];
                    String destination = d[1];
                    Date startTime = new Date(Long.parseLong(d[2]));
                    Long duration = Long.parseLong(d[3]);
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "New CDR");
                    Platform.runLater(() -> {
                        if (source.equals(ownExtension)) {
                            bus.post(new AddCdrAndUpdateEvent(destination, startTime.toString(), duration.toString(), true, bus));
                        } else {
                            bus.post(new AddCdrAndUpdateEvent(destination, startTime.toString(), duration.toString(), false, bus));
                        }
                    });
                    break;
                }
                default: {
                    Logger.getLogger(getClass().getName()).log(Level.WARNING, "Command not recognized");
                    break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
