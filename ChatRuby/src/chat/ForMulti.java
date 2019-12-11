/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vutrunganh
 */
public class ForMulti extends Thread{
    Socket socket;
    public ForMulti(Socket socket){
        this.socket = socket;
    }
    public void run(){
        DataInputStream input;
        DataOutputStream output;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            while(true){
                String in = input.readLine();
                output.writeUTF(in);
            }
        } catch (IOException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }
}
