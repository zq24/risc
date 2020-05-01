package ece651.risc.team11.GUIConsole;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomSceneCreator {
    private Button readyButton;
    private List<Pane> playerCardList;
    private Pane controlPanel;
    private ComboBox<Integer> roomSizeComboBox;
    private List<Label> nameTagList;
    private TextArea chatOutput;
    private TextField chatInput;
    private Button chatButton;

    public WaitingRoomSceneCreator() {
        this.playerCardList = new ArrayList<>();
        this.nameTagList = new ArrayList<>();
    }

    public Pane makeScene() {
        BorderPane borderPane = new BorderPane();
        chatOutput = new TextArea();
        chatInput = new TextField();
        chatOutput.setEditable(false);
        chatOutput.setWrapText(true);
        chatOutput.prefHeightProperty().bind(borderPane.heightProperty().divide(2));
        chatButton = new Button("Send");
        BorderPane chatbox = new BorderPane();
        chatbox.setTop(chatOutput);
        chatbox.setCenter(chatInput);
        chatbox.setRight(chatButton);

        controlPanel = makeControlPanel();

        readyButton = new Button("Ready");
        readyButton.setFont(Font.font(20));

        VBox vBox = new VBox(30);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().add(chatbox);
        vBox.getChildren().add(controlPanel);
        vBox.getChildren().add(readyButton);

        GridPane waitingSlot = new GridPane();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 3; i++) {
                Pane playerCard = makePlayerCard(i, j);
                waitingSlot.add(playerCard, i, j);
                playerCardList.add(playerCard);
            }
        }
        borderPane.setRight(vBox);
        borderPane.setCenter(waitingSlot);
        vBox.prefWidthProperty().bind(borderPane.widthProperty().divide(4));
        return borderPane;
    }

    private Pane makeControlPanel() {
        GridPane gridPane = new GridPane();
        roomSizeComboBox = new ComboBox<>();
        roomSizeComboBox.getSelectionModel().selectFirst();

        gridPane.add(new Label("Room Configuration Panel:"), 0, 0, 3, 1);
        gridPane.add(new Label("Room Size"), 0, 3, 2, 1);
        gridPane.add(roomSizeComboBox, 2, 3);
        gridPane.setDisable(true);
        return gridPane;
    }

    private Pane makePlayerCard(int i, int j) {
        Pane pane = new Pane();
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.rgb(135, 206, 250, 0.5));
        rectangle.setStroke(Color.BLACK);
        rectangle.setArcWidth(30);
        rectangle.setArcHeight(30);
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());

        Rectangle cover = new Rectangle();
        cover.setFill(Color.rgb(135, 206, 250));
        cover.setStroke(Color.BLACK);
        cover.setArcWidth(30);
        cover.setArcHeight(30);
        cover.widthProperty().bind(pane.widthProperty());
        cover.heightProperty().bind(pane.heightProperty());

        VBox vBox = new VBox(30);
        vBox.setAlignment(Pos.CENTER);
        ImageView avatar = new ImageView(String.format("avatars/png/avatar-%d.png", i * 2 + j + 1));
        avatar.setPreserveRatio(true);
        avatar.fitWidthProperty().bind(rectangle.widthProperty());
        Label playerTag = new Label();
        nameTagList.add(playerTag);
        vBox.getChildren().add(avatar);
        vBox.getChildren().add(playerTag);

        Label readyStamp = new Label("READY");
        readyStamp.setFont(Font.font(72));
        readyStamp.setTextFill(Color.RED);
        readyStamp.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7)");
        readyStamp.setRotate(-30);
        readyStamp.translateXProperty().bind(rectangle.widthProperty().divide(5));
        readyStamp.translateYProperty().bind(rectangle.heightProperty().divide(2));
        readyStamp.setOpacity(0);

        pane.getChildren().add(rectangle);
        pane.getChildren().add(vBox);
        pane.getChildren().add(readyStamp);
        pane.getChildren().add(cover);
        return pane;
    }

    public String getText() {
        String text = chatInput.getText();
        chatInput.clear();
        return text;
    }

    public void appendText(String text) {
        chatOutput.appendText(text);
    }

    public Button getReadyButton() {
        return readyButton;
    }

    public List<Pane> getPlayerCardList() {
        return playerCardList;
    }

    public Pane getControlPanel() {
        return controlPanel;
    }

    public ComboBox<Integer> getRoomSizeComboBox() {
        return roomSizeComboBox;
    }

    public List<Label> getNameTagList() {
        return nameTagList;
    }

    public Button getChatButton() {
        return chatButton;
    }

}