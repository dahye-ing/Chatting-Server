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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppController implements Initializable {
    @FXML private Button startBtn;
    @FXML private TextArea screen;

    public static HashMap<String, Socket> clientList;
    public static ExecutorService threadPool;

    ServerSocket serverSocket = null;
    Socket socket = null;

    public void startServer(){
        clientList = new HashMap<>();
        Collections.synchronizedMap(clientList);

        try{
            serverSocket = new ServerSocket(1119);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runnable thread = () -> {
            try{

                while(true){
                    Platform.runLater(()-> {
                        screen.appendText("[[서버 시작]]\n");
                        startBtn.setText("서버 OFF");
                    });
                    socket = serverSocket.accept();
                    Thread thread1 = new Thread(new Client(socket));
                    thread1.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        threadPool = Executors.newCachedThreadPool();
        threadPool.submit(thread);

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
            if(threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void onStartButtonClick() {
        if(startBtn.getText().equals("서버 ON")){
            startServer();

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
