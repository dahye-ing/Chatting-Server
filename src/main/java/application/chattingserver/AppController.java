package application.chattingserver;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AppController {
    @FXML
    private Button startBtn;

    @FXML
    protected void onStartButtonClick() {
        if(startBtn.getText().equals("서버 ON")){
            ChattingMain c = new ChattingMain();
            c.startServer();
        }
    }
}