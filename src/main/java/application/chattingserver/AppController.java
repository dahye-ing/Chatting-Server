package application.chattingserver;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;


public class AppController implements Initializable {
    @FXML private Button startBtn;
    @FXML private TextArea screen;

    static HashMap<String, Socket> clientList;
    ServerSocket serverSocket = null;

    public void startServer(){
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

    public class ServerThread implements Runnable{
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

    public void stopServer(){
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

    protected void onStartButtonClick() {
        if(startBtn.getText().equals("서버 ON")){
            startServer();
            Platform.runLater(()-> {
                screen.appendText("[[서버 시작]]\n");
                startBtn.setText("서버 OFF");
            });

        } else{
            stopServer();
            Platform.runLater(()-> {
                screen.appendText("[[서버 종료]]\n");
                startBtn.setText("서버 ON");
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startBtn.setOnAction(event -> onStartButtonClick());
    }
}