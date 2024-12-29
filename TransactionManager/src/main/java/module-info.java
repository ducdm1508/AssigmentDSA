module org.example.transactionmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.transactionmanager.Controller to javafx.fxml;
    exports org.example.transactionmanager;
}