module application.chattingserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens application.chattingserver to javafx.fxml;
    exports application.chattingserver;
}