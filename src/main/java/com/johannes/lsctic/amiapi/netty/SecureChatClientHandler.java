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
import com.johannes.lsctic.messagestage.ErrorMessage;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.AddCdrAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.CdrCountEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.SetStatusEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.ReceivedOwnExtensionEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.UserLoginStatusEvent;
import com.johannes.lsctic.panels.gui.plugins.pluginevents.PluginLicenseApprovedEvent;
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
            Platform.runLater(()->bus.post(new UserLoginStatusEvent(true, msg.substring(4))));
        } else if ("lfai".equals(msg)) {
            Platform.runLater(()->bus.post(new UserLoginStatusEvent(false, "")));
        } else if (msg.startsWith("owne")) {
            Platform.runLater(()->bus.post(new ReceivedOwnExtensionEvent(msg.substring(4))));
            ownExtension = msg.substring(4);
        } else if ("chfa".equals(msg)) {
            displayPasswordChangeFailedError();
        } else {
            try {
                String chatInput = msg;
                int op = Integer.parseInt(chatInput.substring(0, 3));
                String param = chatInput.substring(3, chatInput.length());
                switch (op) {
                    case 0:
                        updateStatus(param);
                        break;
                    case 10:
                        createAndPropagateCdrField(param);
                        break;
                    case 11:
                        Platform.runLater(()->bus.post(new CdrCountEvent(Integer.valueOf(param))));
                        break;
                    case 12:
                        Platform.runLater(()->{});
                        break;
                    case 15:
                        Platform.runLater(() -> bus.post(new PluginLicenseApprovedEvent(param,0,false)));
                        break;
                    case 16:
                        displayLicenseError(param);
                        break;
                    case 17:
                        displayLicenseExceed(param);
                        break;
                    case 18:
                        displayTestPhaseEnded(param);
                        break;
                    case 19:
                        displayTestPhaseWillEndSoon(param);
                        break;
                    default:
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Command not recognized");
                        break;
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
        Platform.runLater(()->bus.post(new SetStatusEvent(state, intern)));
        Logger.getLogger(getClass().getName()).log(Level.INFO, "New State");
    }


    private void createAndPropagateCdrField(String param) {
        String[] d = param.split(":");
        String source = d[0];
        String destination = d[1];
        long timeStamp = Long.parseLong(d[2]);
        Date stored = new Date(timeStamp);
        String date = dateFormatDB.format(stored);
        Long duration = Long.parseLong(d[3]);
        String id = d[4];
        Platform.runLater(() -> {
            if (source.equals(ownExtension)) {
                bus.post(new AddCdrAndUpdateEvent(destination, date, duration.toString(), true, timeStamp));
            } else {
                bus.post(new AddCdrAndUpdateEvent(source, date, duration.toString(), false, timeStamp));
            }
        });
    }

    private void displayLicenseError(String param) {
        String errorMessage = "Could not verify the license for " + param + ". Is the right certificate for your license installed on the server?";
        Platform.runLater(() -> new ErrorMessage(errorMessage));
    }

    private void displayLicenseExceed(String param) {
        String errorMessage = "Amount of registered users for plugin " + param + " exceeds the amount purchased. Please consider to buy more licenses.";
        Platform.runLater(() -> new ErrorMessage(errorMessage));
    }
    private void displayTestPhaseEnded(String param) {
        String errorMessage = "The test phase for " + param + " is over. Please consider to buy licenses.";
        Platform.runLater(() -> new ErrorMessage(errorMessage));
    }
    private void displayTestPhaseWillEndSoon(String param) {
        String[] parameter = param.split(";");
        if(Integer.valueOf(parameter[1])<10) {
            String errorMessage = "The test phase for " + parameter[0] + " will be over in " + parameter[1] + " days. Please consider to buy licenses.";
            Platform.runLater(() -> new ErrorMessage(errorMessage));
        }
        Platform.runLater(()-> bus.post(new PluginLicenseApprovedEvent(parameter[0],Integer.valueOf(parameter[1]),true)));
    }


    private void displayPasswordChangeFailedError() {
        Platform.runLater(() -> new ErrorMessage("Password change not possible. Old password seems to be wrong. " +
                "You were logged out. You need to login again in the server settings with your old password."));
    }

}
