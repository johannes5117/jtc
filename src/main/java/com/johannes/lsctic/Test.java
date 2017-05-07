package  com.johannes.lsctic;
import com.google.common.eventbus.EventBus;
import com.sun.awt.AWTUtilities;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
    // Bereich für den Hook
    private boolean strg = false;
    private boolean shift = false;
    private boolean five = false;
    private boolean escape = false;
    private JDialog frame;

    private void initAndShowGUI() {
        // This method is invoked on the EDT thread
        frame= new JDialog();
        frame.setUndecorated(true);

        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setBackground(new Color(0,0,255,0));
        frame.add(fxPanel);
        frame.setBackground(new Color(0, 0, 255, 0));
        frame.setSize(337, 380);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        frame.setLocation(new Point(
                Integer.valueOf((int) (primaryScreenBounds.getWidth() - frame.getWidth())),
                Integer.valueOf((int) (primaryScreenBounds.getHeight() - frame.getHeight()))));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setType(Window.Type.UTILITY);
        frame.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = null;
        try {
            scene = createScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fxPanel.setScene(scene);
        Logger.getLogger(getClass().getName()).info("width: "+scene.getWidth()+" height: "+scene.getHeight());
        frame.setSize(new Dimension((int)scene.getWidth(), (int)scene.getHeight()));
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        frame.setLocation(new Point(
                Integer.valueOf((int) (primaryScreenBounds.getWidth() - frame.getWidth())),
                Integer.valueOf((int) (primaryScreenBounds.getHeight() - frame.getHeight()))));
    }

    private  Scene createScene() throws IOException {
        generateTray();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = loader.load();
        FXMLController controller = loader.getController();
        EventBus eventBus = new EventBus();
        controller.startApp(eventBus);
        Scene scene = new Scene(root);
        scene.setFill(null);

        Font.loadFont(MainApp.class.getResource("/styles/Roboto-Light.ttf").toExternalForm(),13);
        //scene.getStylesheets().clear();
        scene.getStylesheets().add("/styles/Styles.css");
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

        return (scene);
    }

    public static void main(String[] args) {
        Test t = new Test();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                t.initAndShowGUI();
            }
        });
    }
    private void generateTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon
                = new TrayIcon(Toolkit.getDefaultToolkit().getImage("/pics/down.png"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

}