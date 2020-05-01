package ece651.risc.team11.GUIConsole;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StartSceneCreator {
    private Button startButton;
    private Button quitButton;

    public StartSceneCreator() {
    }

    public Pane makeScene(Scene scene) {
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView("no-text-pexels-photo-961334.png");
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(scene.getHeight());
        imageView.setOpacity(0.7);
        stackPane.getChildren().add(imageView);

        startButton = new Button("Start Game");
        startButton.setFont(Font.font(20));

        quitButton = new Button("Quit Game");
        quitButton.setFont(Font.font(20));

        Label label = new Label("R I S C");
        label.setFont(Font.font(72));
        label.setTextFill(Color.WHITE);

        VBox vBox = new VBox(50);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, startButton, quitButton);
        ImageView shield = new ImageView("shield-308943_1280.png");
        shield.setPreserveRatio(true);
        shield.setFitWidth(scene.getHeight() / 2);
        shield.setOpacity(0.8);

        stackPane.getChildren().addAll(shield);
        stackPane.getChildren().add(vBox);
        return stackPane;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getQuitButton() {
        return quitButton;
    }

}