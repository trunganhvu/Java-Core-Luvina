/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author vutrunganh
 */
public class MainServer {
    static Vector<ClientHandler> ar = new Vector<>();
    static int i = 0;
    public static Map<String, String> map = new TreeMap<String, String>();

    public static Map<String, String> getMap() {
        return map;
    }
    
    static void init() throws IOException{
        ServerSocket server;
        Socket socket;
        server = new ServerSocket(8000);
        while(true){
            socket = server.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            //map.put(socket.getRemoteSocketAddress().toString(), "client "+i);
            ClientHandler client = new ClientHandler("client "+i, socket, input, output);
            Thread thread = new Thread(client);      
            ar.add(client);
            thread.start();
            i++;
            //System.out.println("hello");
            
        }
    }
    public static void main(String[] args) throws IOException {
        init();
    }
    
}
