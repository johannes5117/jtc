package com.johannes.lsctic.amiapi;

import com.johannes.lsctic.FXMLController;
import com.johannes.lsctic.fields.HistoryField;
import com.johannes.lsctic.fields.InternField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class ServerConnectionHandler {
    private final String adresse = "localhost";
    private final int port = 12350;
    private final Socket s;
    private ClientThread t;
    private final Map<String, InternField> internNumbers;
    private FXMLController fcont;
    public ServerConnectionHandler(Map<String, InternField> internNumbers, FXMLController fcont) throws IOException {
        s = new Socket(adresse, port); 
         t = new ClientThread(s);
        new Thread(t).start();
        this.internNumbers = internNumbers;
        this.fcont = fcont;
    }
    public void sendBack(String msg) {
        t.sendBack(msg);
    }
    public void aboExtension(String ext) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Status abo for: {0}", ext);
        t.sendBack("000"+ext);
    }
    public void deAboExtension(String ext) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Status deabo for: {0}", ext);

        t.sendBack("001"+ext);
    }
    
    class ClientThread implements Runnable
    {
        Socket threadSocket;
        PrintWriter output;
         
        //This constructor will be passed the socket
        public ClientThread(Socket socket)
        {
            //Here we set the socket to a local variable so we can use it later
            threadSocket = socket;
        }
         
        @Override
        public void run()
        {
            //All this should look familiar
            try {
                //Create the streams
               
                //Tell the client that he/she has connected
                BufferedReader input = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));

                boolean notEndedYet = true;
                while (notEndedYet) {
                    //This will wait until a line of text has been sent
                    String chatInput = input.readLine();
                    switch (chatInput) {
                        case "logoff":
                            notEndedYet = false;
                            output.println("success");
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
                                    if(source.equals(fcont.getOwnExtension()+"")){
                                        fcont.addCdrAndUpdate(new HistoryField(destinatnion, startTime.toString(), duration.toString(), true));
                                    } else {
                                        fcont.addCdrAndUpdate(new HistoryField(destinatnion, startTime.toString(), duration.toString(), false));
                                    }
                                }
                                default:
                                    System.out.println("i liegt nicht zwischen null und drei");
                            }   break;
                    }
                }
                threadSocket.close();
            } catch(IOException exception) {
                System.out.println("Error: " + exception);
            }
        }
        private void sendBack(String msg) {
            try {
                output = new PrintWriter(threadSocket.getOutputStream(), true);
                output.println(msg);
                output.flush();
            } catch (IOException ex) {
                Logger.getLogger(ServerConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
