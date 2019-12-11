/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import redis.clients.jedis.Jedis;

/**
 *
 * @author vutrunganh
 */
public class ClientHandler implements Runnable {

    private String name;
    final DataInputStream input;
    final DataOutputStream output;
    Socket socket;
    boolean isloggedin;
    static String list;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientHandler(String name, Socket socket, DataInputStream in, DataOutputStream out) {
        this.name = name;
        this.socket = socket;
        this.input = in;
        this.output = out;
        isloggedin = true;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                Jedis jedis = new Jedis("localhost");
                System.out.println(jedis.ping());
                jedis.select(1);
                received = input.readUTF();
                StringTokenizer stringtokenizer = new StringTokenizer(received, "#");
                String mess = stringtokenizer.nextToken(); //cho mess vào 1 list hay mảng rồi toString writeUTF
                String recipient = stringtokenizer.nextToken();
                String listClient = "";
                for (ClientHandler client : MainServer.ar) {
                    listClient += client.getName() + ",";
                }
//                InetAddress clientAddress = socket.getInetAddress();
//                System.out.println(socket.getRemoteSocketAddress().toString());
                
                for (ClientHandler client : MainServer.ar) {
                    if (client.name.equals(recipient) && client.isloggedin == true) {
                        client.output.writeUTF(this.name + " : " + mess + "#" + listClient);
                        String key = this.name + client.getName();
                        key = key.replaceAll("\\s", "");
                        Map<String, String> map = new HashMap<String,String>();
                        map.put(this.name, mess);
//                        jedis.hset(key, this.name, mess);
                        jedis.lpush(key, this.name+"#"+mess);
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

//            this.input.close();
//            this.output.close();
    }

}
