/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.amiapi;

/**
 *
 * @author Johannes
 */
import com.johannes.lsctic.FXMLController;
import com.johannes.lsctic.fields.HistoryField;
import com.johannes.lsctic.fields.InternField;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Handles a client-side channel.
 */
public class SecureChatClientHandler extends SimpleChannelInboundHandler<String> {
    private final FXMLController fxml;
    private final Map<String, InternField> internNumbers;
    public SecureChatClientHandler(FXMLController fxml,Map<String, InternField> internNumbers) {
        this.fxml = fxml;
        this.internNumbers = internNumbers;
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.err.println(msg);
       

                boolean notEndedYet = true;
                while (notEndedYet) {
                    //This will wait until a line of text has been sent
                    String chatInput = msg;
                    switch (chatInput) {
                        case "logoff":
                            notEndedYet = false;
                           
                            break;
                        case "success":
                            System.out.println("Erfolgreich Verbunden");
                            break;
                        default:
                            int op = Integer.valueOf(chatInput.substring(0, 3));
                            String param = chatInput.substring(3,chatInput.length());
                            Logger.getLogger(getClass().getName()).info(chatInput);
                            switch(op) {
                                case 0: {
                                    String[] d = param.split(":");
                                    String intern = d[0];
                                    int status = Integer.valueOf(d[1]);
                                    internNumbers.get(intern).setStatus(status);
                                    break;
                                }
                                case 10: {
                                    String[] d = param.split(":");
                                    String source = d[0];
                                    String destinatnion = d[1];
                                    Date startTime = new Date(Long.parseLong(d[2]));
                                    Long duration = Long.parseLong(d[3]);
                                    int disposition = Integer.valueOf(d[4]);
                                    System.out.println("CDR: von: "+source+" nach: "+destinatnion+" am "+startTime.toString()+" dauer "+ duration+" dispo: "+disposition);
                                    if(source.equals(fxml.getOwnExtension()+"")){
                                        fxml.addCdrAndUpdate(new HistoryField(destinatnion, startTime.toString(), duration.toString(), true));
                                    } else {
                                        fxml.addCdrAndUpdate(new HistoryField(destinatnion, startTime.toString(), duration.toString(), false));
                                    }
                                }
                                default:
                                    System.out.println("i liegt nicht zwischen null und drei");
                            }   break;
                    }
                }
            
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
    
    
    
}
