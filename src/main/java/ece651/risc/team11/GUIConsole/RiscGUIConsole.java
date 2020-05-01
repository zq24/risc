package ece651.risc.team11.GUIConsole;

import ece651.risc.team11.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RiscGUIConsole implements RiscConsole {
    GUIConsolSceneController sceneController;
    RiscClient client;
    private List<Instruction> instructionList;

    public RiscGUIConsole(RiscClient client,
                          Stage stage) {
        this.client = client;
        instructionList = new ArrayList<>();
        Scene scene = new Scene(new Pane(), 1250, 1000);
        scene.getStylesheets().add("styleSheet.css");
        sceneController = new GUIConsolSceneController(scene, this);
        ArrayList<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            colorList.add(i);
        }
        //sceneController.setGameScene(new RiscMap(3, 4, colorList));
        sceneController.setStartScene();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void displayText(String message) {
        Platform.runLater(() -> {
            if (sceneController.getGamePhase().equals(GamePhase.WAIT_TO_START)) {
                sceneController.getWaitingRoomSceneCreator().appendText(message + "\n");

            } else {
                sceneController.getGameSceneCreator().appendText(message + "\n");
            }
        });
    }

    @Override
    public void decideRoomSize(int minNumOfPlayers, int maxNumOfPlayers) {
        Platform.runLater(() -> {
            ComboBox<Integer> roomSizeComboBox =
                    sceneController.getWaitingRoomSceneCreator().getRoomSizeComboBox();
            for (int i = minNumOfPlayers; i <= maxNumOfPlayers; i++) {
                roomSizeComboBox.getItems().add(i);
            }
            roomSizeComboBox.getSelectionModel().selectFirst();
            roomSizeComboBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                client.sendMessageToServer(new RiscMessage(RiscMessageType.ROOMSIZE, newValue));
            });
            sceneController.getWaitingRoomSceneCreator().getControlPanel().setDisable(false);
        });
    }

    @Override
    public void displayMap(RiscMap riscMap, int playerID) {
        Platform.runLater(() -> {
            sceneController.setGameScene(riscMap);
        });
    }

    @Override
    public void setPlayerReady(List<RiscPlayerInRoom> playerList) {
        Platform.runLater(() -> {
            for (int i = 0; i < sceneController.getWaitingRoomSceneCreator().getPlayerCardList().size(); i++) {
                Pane playerCard =
                        sceneController.getWaitingRoomSceneCreator().getPlayerCardList().get(i);
                if (i < playerList.size()) {
                    playerCard.setOpacity(1);
                    RiscPlayerInRoom player = playerList.get(i);
                    Node cover = playerCard.getChildren().get(
                            playerCard.getChildren().size() - 1);
                    if (player == null) {
                        cover.setOpacity(1);
                    } else {
                        cover.setOpacity(0);
                        sceneController.setPlayerName(i,
                                String.format("%d", player.getPlayerID()));
                        Node playerReadySign = playerCard.getChildren().get(
                                playerCard.getChildren().size() - 2);
                        playerReadySign.setOpacity(player.isReady() ? 1 : 0);
                    }
                } else {
                    playerCard.setOpacity(0.1);
                }
            }
        });
    }

    @Override
    public void enterAllActions() {
        client.sendMessageToServer(new RiscMessage(RiscMessageType.ACTION, instructionList));
        instructionList.clear();
    }

    @Override
    public void showWin(int playerID) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(String.format("Player %d won the game!", playerID));

            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void updateMap(RiscMap riscMap) {

    }

    @Override
    public void setPlayerInfo(RiscPlayerInRoom player) {
        Platform.runLater(() -> sceneController.getGameSceneCreator().setPlayerInfo(player));
    }

    public void onReadyButtonPress(Button readyButton) {
        if (readyButton.getText().equals("Ready")) {
            client.sendMessageToServer(new RiscMessage(RiscMessageType.READY, true));
            readyButton.setText("Cancel");
        } else {
            client.sendMessageToServer(new RiscMessage(RiscMessageType.READY, false));
            readyButton.setText("Ready");
        }
    }

    public void enterARoom() {
        client.connect();
        client.sendMessageToServer(new RiscMessage(RiscMessageType.READY, false));
    }

    public void exitARoom() {
        Platform.runLater(() -> sceneController.setStartScene());
    }

    public void addInstruction(Instruction instruction) {
        instructionList.add(instruction);
    }

    public void sendChatMessage(String text) {
        client.sendMessageToServer(new RiscMessage(RiscMessageType.INFO, text));
    }
}