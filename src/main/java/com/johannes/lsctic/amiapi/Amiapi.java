/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.amiapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class Amiapi {

    private final String server;
    private final String user;
    private final String passwort;
    private final int port;
    private Socket socket;
    private DataOutputStream wr;
    private BufferedReader buffin;
    private ArrayList<String> fifo;
    private boolean found;
    private int actId;
    private boolean loggedin;

    public Amiapi(String server, String user, String passwort, int port) {
        this.server = server;
        this.user = user;
        this.passwort = passwort;
        this.port = port;
        this.loggedin = false;
    }

    public void login() throws IOException {

        connect(server, port);
        send("Action: Login\r\n");
        send("username: " + user + "\r\n");
        send("secret: " + passwort + "\r\n");
        send("ActionID: 1\r\n");
        send("\r\n");
        loggedin = true;
    }

    public void dial(String extension, String number)
            throws IOException {
        login();
        send("Action: Originate\r\n");
        send("Channel: sip/" + extension + "\r\n");
        send("Exten: " + number + "\r\n");
        //FIXME 
        //  send("Context: from-internal\r\n");
        send("Async: yes\r\n\r\n");
    }

    public int getStatus(String extension)
            throws IOException {
        Random rand = new Random();
        int tActId = rand.nextInt((1000000 - 1) + 1) + 1;
        send("Action: ExtensionState\r\n");
        send("Exten: " + extension + "\r\n");
        send("Context: SIP-PHONE-134761875356fa722faecf5\r\n");
        send("ActionID: " + tActId + "\r\n");
        send("\r\n");
        actId = tActId;
        while (found == false) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Amiapi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        found = false;

        String b = fifo.get(fifo.indexOf("ActionID: " + tActId + "") + 5);
        fifo.clear();
        return Integer.valueOf(b.substring(8, b.length()));
    }

    public void send(String request) throws IOException {
        
           
                wr.writeBytes(request);
   
                
            
        
    }

    public void receive() throws IOException {
        while (true) {
            String out;
            while ((out = buffin.readLine()) != null) {

                if (out.equals("ActionID: " + actId)) {
                    found = true;
                }

                fifo.add(out);

            }

        }
    }

    public void connect(String server, int port)
            throws IOException {

        InetAddress addr = InetAddress.getByName(server);
        SocketAddress sockaddr = new InetSocketAddress(addr, port);
        socket = new Socket();
        socket.connect(sockaddr);
        wr = new DataOutputStream(socket.getOutputStream());
        buffin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        fifo = new ArrayList<>();
        Thread t;
        t = new Thread(() -> {
            try {
                receive();
            } catch (IOException ex) {
                Logger.getLogger(Amiapi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        t.start();
    }

}
