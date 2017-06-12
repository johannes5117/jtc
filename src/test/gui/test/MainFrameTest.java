package gui.test;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.FXMLController;
import com.johannes.lsctic.MainApp;
import com.johannes.lsctic.panels.gui.fields.otherevents.CloseApplicationSafelyEvent;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by johannesengler on 30.05.17.
 */
public class MainFrameTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
            Parent root = loader.load();
            FXMLController controller = loader.getController();
            EventBus eventBus = new EventBus();
            controller.startApp(eventBus);
            controller.setStage(stage);
            Scene scene = new Scene(root);
            Font.loadFont(MainApp.class.getResource("/styles/Roboto-Light.ttf").toExternalForm(),13);

            scene.getStylesheets().add("/styles/Styles.css");
            stage.setTitle("JavaFX and Maven");

            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.setScene(scene);

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.show();
            stage.setX(primaryScreenBounds.getWidth() - scene.getWidth());
            stage.setY(primaryScreenBounds.getHeight() - scene.getHeight());

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Logger.getLogger(getClass().getName()).info("Window Closed");
                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException ex) {
                        Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    eventBus.post(new CloseApplicationSafelyEvent());
                    stage.close();
                    Platform.exit();
                    System.exit(0);
                    //TODO: Find way to securely shutdown program
                }
            });
    }

    @Test
    public void clickOn() {




    }


}
