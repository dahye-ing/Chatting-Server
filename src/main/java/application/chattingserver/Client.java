package application.chattingserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import static application.chattingserver.ChattingMain.clientList;

public class Client{
    Socket socket;

    public Client(Socket socket){
        this.socket = socket;
        Thread thread = new Thread(new ServerReceiver(socket));
        thread.run();
    }

    class ServerReceiver implements Runnable{
        DataInputStream in;
        DataOutputStream out;

        ServerReceiver(Socket socket){
            try{
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) { }
        }

        @Override
        public void run() {
            String name = "";
            try{
                while(true){
                    name = in.readUTF();
                    if(clientList.containsKey(name)){
                        out.writeUTF("이미 존재하는 닉네임입니다.");
                    }else{
                        clientList.put(name, socket);
                        serverSender("[["+name+"님이 들어왔습니다]]");
                        System.out.println("현재 서버 접속자 수는 "+clientList.size()+"입니다");
                        break;
                    }
                } //while
                while(in!=null){
                    serverSender(in.readUTF());
                }
            } catch (IOException e) {

            }finally {
                clientList.remove(name);
                serverSender("[["+name+"님이 나갔습니다]]");
                System.out.println("현재 서버 접속자 수는 "+clientList.size()+"입니다");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } //try
            } //try
        } // run
    } //ServerReceiver

    public void serverSender(String msg){
        Iterator it = clientList.keySet().iterator();
        while(it.hasNext()){
            try{
                DataOutputStream out = new DataOutputStream(clientList.get(it.next()).getOutputStream());
                out.writeUTF(msg);
            } catch (IOException e) { }
        } // while
    } // serverSender
} // class
