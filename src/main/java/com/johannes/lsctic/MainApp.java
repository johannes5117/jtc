package com.johannes.lsctic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        // Get the logger for "org.jnativehook" and set the level to warning.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);

        GlobalScreen.registerNativeHook();
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
                stage.close();
                Platform.exit();
                //TODO: Find way to securely shutdown program
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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
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
        } else if (five && strg && shift == true) {
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
