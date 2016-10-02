/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.amiapi;

import com.johannes.lsctic.PhoneNumber;
import com.johannes.lsctic.fields.InternField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author johannesengler
 */
public class ServerConnectionHandler {
    private final String adresse = "localhost";
    private final int port = 12350;
    private final Socket s;
    private final Map<Integer, InternField> internNumbers;
    public ServerConnectionHandler(Map<Integer, InternField> internNumbers) throws IOException {
        s = new Socket(adresse, port); 
        ClientThread t = new ClientThread(s);
        new Thread(t).start();
        this.internNumbers = internNumbers;
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
                output = new PrintWriter(threadSocket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
                sendBack("000702");
                //Tell the client that he/she has connected
                output.println("success");
                boolean notEndedYet = true;
                while (notEndedYet) {
                    //This will wait until a line of text has been sent
                    String chatInput = input.readLine();
                    if(chatInput.equals("logoff")) {
                        notEndedYet = false;
                        output.println("success");
                    } else if(chatInput.equals("success")) {
                        System.out.println("Erfolgreich Verbunden");
                    }else {
                        int op = Integer.valueOf(chatInput.substring(0, 2));
                        String param = chatInput.substring(3,chatInput.length());
                        System.out.println(op);
                        switch(op) {
                            case 0:
                                int intern = Integer.valueOf(param.substring(0,param.length()-1));
                                int status = Integer.valueOf(param.substring(param.length()-1,param.length()));
                                internNumbers.get(intern).setStatus(status);
                                System.out.println("Für: "+intern+" setze: "+status);
                                break;
                            default: 
                                System.out.println("i liegt nicht zwischen null und drei"); 
                        }
                    }
                    System.out.println(chatInput);
                }
                threadSocket.close();
            } catch(IOException exception) {
                System.out.println("Error: " + exception);
            }
        }
        private void sendBack(String msg) {
            output.println(msg);
        }
    }
}
