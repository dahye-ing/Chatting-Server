package application.chattingserver;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import static application.chattingserver.ChattingMain.startServer;
import static application.chattingserver.ChattingMain.stopServer;

public class AppController {
    @FXML
    private Button startBtn;

    @FXML
    private TextArea screen;

    @FXML
    protected void onStartButtonClick() {
        if(startBtn.getText().equals("서버 ON")){
            startServer();
            Platform.runLater(()-> {
                screen.appendText("[[서버 시작]]\n");
                startBtn.setText("서버 OFF");
            });

        } else{
            //stopServer();
            Platform.runLater(()-> {
                screen.appendText("[[서버 종료]]\n");
                startBtn.setText("서버 ON");
            });
        }
    }
}