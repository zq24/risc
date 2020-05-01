package ece651.risc.team11.GUIConsole;

import ece651.risc.team11.GamePhase;
import ece651.risc.team11.RiscMap;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public class GUIConsolSceneController {
    private Scene scene;
    int size = 1250;
    private RiscGUIConsole console;
    private GamePhase gamePhase;
    private StartSceneCreator startSceneCreator;
    private WaitingRoomSceneCreator waitingRoomSceneCreator;
    private GameSceneCreator gameSceneCreator;

    //public GUIConsolSceneController() {}

    public GUIConsolSceneController(Scene scene, RiscGUIConsole console) {
        this.scene = scene;
        this.console = console;
        startSceneCreator = new StartSceneCreator();
        waitingRoomSceneCreator = new WaitingRoomSceneCreator();
        gameSceneCreator = new GameSceneCreator();
    }

    public void setStartScene() {
        gamePhase = GamePhase.START_GAME;
        scene.setRoot(startSceneCreator.makeScene(scene));
        startSceneCreator.getStartButton().setOnMouseClicked(mouseEvent -> {
            setWaitingRoomGameScene();
            console.enterARoom();
        });
        startSceneCreator.getQuitButton().setOnMouseClicked(mouseEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void setWaitingRoomGameScene() {
        gamePhase = GamePhase.WAIT_TO_START;
        scene.setRoot(waitingRoomSceneCreator.makeScene());
        waitingRoomSceneCreator.getReadyButton().setOnMouseClicked(
                mouseEvent -> console.onReadyButtonPress(
                        waitingRoomSceneCreator.getReadyButton()));
        waitingRoomSceneCreator.getChatButton().setOnMouseClicked(
                mouseEvent -> {
                    console.sendChatMessage(gameSceneCreator.getText());
                });
    }

    public void setGameScene(RiscMap riscMap) {
        gamePhase = GamePhase.PLAY_TURN_ORDER_VALIDATION_CHECK;
        if (gameSceneCreator.getMapGraphic() == null) {
            scene.setRoot(gameSceneCreator.makeScene());
        }
        gameSceneCreator.paintMap(riscMap);
        for (int i = 0; i < gameSceneCreator.getMapGraphic().getTerritoryImageBlocks().size(); i++) {
            ImageView territoryImage =
                    gameSceneCreator.getMapGraphic().getTerritoryImageBlocks().get(i);
            int id = i;
            territoryImage.setOnMouseClicked(mouseEvent -> {
                for (ImageView imageView : gameSceneCreator.getMapGraphic().getTerritoryImageBlocks()) {
                    imageView.setEffect(null);
                }
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(0.5);
                territoryImage.setEffect(colorAdjust);
                gameSceneCreator.setTerritoryInfo(riscMap.getTerritoryList().get(id), id);
                gameSceneCreator.setSelectedTerritory(riscMap.getTerritoryList().get(id));
                gameSceneCreator.setSelectedTerritoryID(id);
            });
        }
        gameSceneCreator.getMoveButton().setOnMouseClicked(
                mouseEvent -> {
                    if (gameSceneCreator.getSelectedTerritory() != null) {
                        gameSceneCreator.actionInputPopupWindow("Move").showAndWait()
                                //.filter(response -> response == ButtonType.OK)
                                .ifPresent(response -> console.addInstruction(response));
                    }
                });
        gameSceneCreator.getAttackButton().setOnMouseClicked(
                mouseEvent -> {
                    if (gameSceneCreator.getSelectedTerritory() != null) {
                        gameSceneCreator.actionInputPopupWindow("Attack").showAndWait()
                                //.filter(response -> response == ButtonType.OK)
                                .ifPresent(response -> console.addInstruction(response));
                    }
                });
        gameSceneCreator.getUpgradeButton().setOnMouseClicked(
                mouseEvent -> {
                    if (gameSceneCreator.getSelectedTerritory() != null) {
                        gameSceneCreator.actionInputPopupWindow("Upgrade").showAndWait()
                                //.filter(response -> response == ButtonType.OK)
                                .ifPresent(response -> console.addInstruction(response));
                    }
                });
        gameSceneCreator.getAllianceButton().setOnMouseClicked(
                mouseEvent -> {
                    gameSceneCreator.actionInputPopupWindow("Alliance").showAndWait()
                            //.filter(response -> response == ButtonType.OK)
                            .ifPresent(response -> console.addInstruction(response));
                });
        gameSceneCreator.getChatButton().setOnMouseClicked(
                mouseEvent -> {
                    console.sendChatMessage(gameSceneCreator.getText());
                });
        gameSceneCreator.getSubmitOrderButton().setOnMouseClicked(
                mouseEvent -> {
                    console.enterAllActions();
                    gameSceneCreator.setSelectedTerritory(null);
                    gameSceneCreator.setSelectedTerritoryID(-1);
                    for (ImageView imageView : gameSceneCreator.getMapGraphic().getTerritoryImageBlocks()) {
                        imageView.setEffect(null);
                    }
                });
    }

    public void setPlayerName(int number, String name) {
        waitingRoomSceneCreator.getNameTagList().get(number).setText(name);
    }


    public StartSceneCreator getStartSceneCreator() {
        return startSceneCreator;
    }

    public WaitingRoomSceneCreator getWaitingRoomSceneCreator() {
        return waitingRoomSceneCreator;
    }

    public GameSceneCreator getGameSceneCreator() {
        return gameSceneCreator;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
}