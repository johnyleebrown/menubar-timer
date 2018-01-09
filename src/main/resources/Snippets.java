import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Created by Greg on 1/4/18.
 */
public class Snippets {

//        System.out.println(workHundreds.getText() + " " + workTens.getText() + " " + workOnes.getText());
//        System.out.println(workTime.get(0) + " " + workTime.get(1) + " " + workTime.get(2));

    FXMLLoader loader = new FXMLLoader(getClass().getResource("Pane.fxml"));
    Pane pane = loader.load();
    paneController = loader.getController();
    Scene scene = new Scene(pane, 534, 148);
    primaryStage.setScene(scene);
}
