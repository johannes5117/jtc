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
import com.johannes.lsctic.panels.gui.fields.callrecordevents.AddCdrAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.SetStatusEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.ReceivedOwnExtensionEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.UserLoginStatusEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a client-side channel.
 */
public class SecureChatClientHandler extends SimpleChannelInboundHandler<String> {
    private EventBus bus;
    private String ownExtension;

    //German date format
    private SimpleDateFormat dateFormatDB = new SimpleDateFormat("HH:mm EEEE dd.MM.yyyy");

    //English
    //private SimpleDateFormat dateFormatDB = new SimpleDateFormat("HH:mm EEEE MM/dd/yyyy");

    public SecureChatClientHandler(EventBus bus) {
        this.bus = bus;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Logger.getLogger(getClass().getName()).info(msg);
        if (msg.startsWith("lsuc")) {
            Logger.getLogger(getClass().getName()).info(msg.substring(4));
            bus.post(new UserLoginStatusEvent(true, msg.substring(4)));
        } else if ("lfai".equals(msg)) {
            bus.post(new UserLoginStatusEvent(false, ""));
        } else if (msg.startsWith("owne")) {
            bus.post(new ReceivedOwnExtensionEvent(msg.substring(4)));
            ownExtension = msg.substring(4);
        } else {
            try {
                String chatInput = msg;
                int op = Integer.valueOf(chatInput.substring(0, 3));
                String param = chatInput.substring(3, chatInput.length());
                switch (op) {
                    case 0: {
                        updateStatus(param);
                        break;
                    }
                    case 10: {
                        createAndPropagateCdrField(param);
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
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    private void updateStatus(String param) {
        String[] d = param.split(":");
        String intern = d[0];
        int state = Integer.parseInt(d[1]);
        bus.post(new SetStatusEvent(state, intern));
        Logger.getLogger(getClass().getName()).log(Level.INFO, "New State");
    }


    private void createAndPropagateCdrField(String param) {
        String[] d = param.split(":");
        String source = d[0];
        String destination = d[1];
        Date stored = new Date(Long.parseLong(d[2]));
        String date = dateFormatDB.format(stored);
        Long duration = Long.parseLong(d[3]);
        Platform.runLater(() -> {
            if (source.equals(ownExtension)) {
                bus.post(new AddCdrAndUpdateEvent(destination, date, duration.toString(), true));
            } else {
                bus.post(new AddCdrAndUpdateEvent(source, date, duration.toString(), false));
            }
        });
    }

}
