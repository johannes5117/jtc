package com.johannes.lsctic;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class MainApp extends Application implements NativeKeyListener {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        generateTray();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = (Parent) loader.load();
        FXMLController controller = (FXMLController) loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.setFill(null);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("JavaFX and Maven");

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.show();
        stage.setX(primaryScreenBounds.getWidth() - scene.getWidth());
        stage.setY(primaryScreenBounds.getHeight() - scene.getHeight());
        this.stage = stage;
        GlobalScreen.registerNativeHook();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        GlobalScreen.addNativeKeyListener(this);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Logger.getLogger(getClass().getName()).info("Window Closed");
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
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
            System.out.println("TrayIcon could not be added.");
        }
    }
    // Bereich für den Hook 
    private boolean strg = false;
    private boolean shift = false;
    private boolean five = false;
    private boolean escape = false;
    /**
     * @param e
     * @see
     * org.jnativehook.keyboard.NativeKeyListener#nativeKeyTyped(org.jnativehook.keyboard.NativeKeyEvent)
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        switch (nke.getKeyCode()) {
            case 1: //Escape Key
                five = false;
                strg = false;
                shift = false;
                escape = true;
                break;
            case 2:
                five = true;
                break;
            case 42:
                strg = true;
                break;
            case 29:
                shift = true;
                break;
            default:
                break;
        }
        if (escape == true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage.toBack();
                    stage.setIconified(true);
                }
            });
            escape = false;
        } else if (five & strg & shift == true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage.toFront();
                    stage.requestFocus();
                    stage.setIconified(false);
                }
            });
            five = false;
            strg = false;
            shift = false;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        switch (nke.getKeyCode()) {
            case 1:
                escape = false;
                break;
            case 2:
                five = false;
                break;
            case 42:
                strg = false;
                break;
            case 29:
                shift = false;
                break;
            default:
                break;
        }
    }
}
