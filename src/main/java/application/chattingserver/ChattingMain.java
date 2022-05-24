package application.chattingserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class ChattingMain extends Application {

    static HashMap<String, Socket> clientList;
    static ServerSocket serverSocket = null;

    public static void startServer(){
        clientList = new HashMap<>();
        Collections.synchronizedMap(clientList);

        try{
            serverSocket = new ServerSocket(1119);
            Thread thread = new Thread(new ServerThread());
            thread.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static class ServerThread implements Runnable{
        Socket socket = null;

        @Override
        public void run() {
            try{

                while(true){
                    socket = serverSocket.accept();
                    Thread thread = new Thread((Runnable) new Client(socket));
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopServer(){
        try{
            Iterator it = clientList.values().iterator();

            while(it.hasNext()){
                Socket socket = clientList.get(it.next());
                socket.close();
                it.remove();
            }
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChattingMain.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 262, 384);
        stage.setTitle("채팅서버");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}