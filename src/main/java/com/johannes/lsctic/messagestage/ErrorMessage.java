package com.johannes.lsctic;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by johannes on 11.04.2017.
 */
public class ErrorMessage extends Application{

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage dialog = primaryStage;
        dialog.initStyle(StageStyle.UNDECORATED);
        VBox box = new VBox();
        box.setSpacing(20);
        box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box);
        scene.getStylesheets().add("styles/Styles.css");

        Image image = new Image("/pics/cancel64.png");
        ImageView imageView = new ImageView(image);

        HBox colorHbox = new HBox();
        colorHbox.setAlignment(Pos.CENTER);
        colorHbox.getChildren().add(imageView);
        colorHbox.setBackground(new Background(new BackgroundFill(Color.INDIANRED, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0))));
        colorHbox.setFillHeight(true);
        colorHbox.setMinHeight(64+64);
        colorHbox.setMinWidth(256+64);


        Text headline = new Text("Oh no!");
        headline.setFont(Font.font ("Segoe UI Semibold", 25));
        Text text = new Text("That is an very unexpected failure. That is an very unexpected failure. That is an very unexpected failure.That is an very unexpected failure.");
        text.wrappingWidthProperty().set(256+64);
        headline.setStyle("-fx-font-smoothing-type: gray;");
        text.setStyle("-fx-font-smoothing-type: gray;");
        text.lineSpacingProperty().set(5);
        text.setFont(Font.font("Segoe UI",12));

        VBox texts = new VBox();
        texts.setPadding(new Insets(0,20,0,20));
        texts.setSpacing(10);
        texts.setAlignment(Pos.CENTER);
        texts.getChildren().addAll(headline,text);


        HBox hBox = new HBox();
        Button buttonAccept = new Button(getUserAgentStylesheet());
        buttonAccept.setText("OK");
        buttonAccept.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialog.close();
            }
        });
       // buttonAccept.getStyleClass().add("message-button");
        buttonAccept.setFont(Font.font("Segoe UI"));


        Button buttonRaise = new Button(getUserAgentStylesheet());
        buttonRaise.setText("Report");
        buttonRaise.getStyleClass().removeAll();
        buttonRaise.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //TODO implement error raising in github or Email
            }
        });
      //  buttonRaise.getStyleClass().add("message-button");
        buttonRaise.setFont(Font.font("Segoe UI"));

        HBox seperatorBox = new HBox();
        HBox.setHgrow(seperatorBox, Priority.ALWAYS);

        hBox.setPadding(new Insets(20));
        hBox.getChildren().addAll(buttonRaise, seperatorBox, buttonAccept);


        box.getChildren().addAll(colorHbox,texts,hBox);

        dialog.setScene(scene);

        dialog.show();
        dialog.sizeToScene();
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);

    }
}
