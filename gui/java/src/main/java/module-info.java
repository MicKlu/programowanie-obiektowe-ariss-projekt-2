module com.example.projectnr2javapython {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.projectnr2javapython to javafx.fxml;
    exports com.example.projectnr2javapython;
}