package ece651.risc.team11.GUIConsole;

import ece651.risc.team11.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class GameSceneCreator {
    private RiscMap riscMap;
    private BorderPane mapPane;
    private MapGraphic mapGraphic;
    private TextArea chatOutput;
    private TextField chatInput;
    private Button chatButton;
    private Button moveButton;
    private Button attackButton;
    private Button upgradeButton;
    private Button allianceButton;
    private Button submitOrderButton;
    private Label playerIDLabel;
    private Label playerFoodLabel;
    private Label playerTechLabel;
    private Label playerColorLabel;
    private Label techResourceLabel;
    private Label foodResourceLabel;
    private GridPane unitsPanel;
    private VBox territoryNamePanel;
    private Territory selectedTerritory;
    private int selectedTerritoryID;

    public GameSceneCreator() {

    }

    public Pane makeScene() {
        mapGraphic = new MapGraphic();
        mapPane = new BorderPane();
        mapPane.setRight(makePlayerInfoPanel());
        mapPane.setBottom(makeTerritoryInfoPanel());
        return mapPane;
    }

    public void paintMap(RiscMap riscMap) {
        Node map = mapGraphic.createMap(riscMap);
        this.riscMap = riscMap;

        /*
        map.setOnScroll(scrollEvent -> {
            map.setTranslateX(map.getTranslateX() + scrollEvent.getDeltaX());
            map.setTranslateY(map.getTranslateY() + scrollEvent.getDeltaY());
        });
         */

        map.setOnZoom(zoomEvent -> {
            map.setScaleX(map.getScaleX() * zoomEvent.getZoomFactor());
            map.setScaleY(map.getScaleY() * zoomEvent.getZoomFactor());
        });
        map.setScaleX(0.8);
        map.setScaleY(0.8);

        mapPane.setCenter(map);
        Set<Integer> idSet = new HashSet<>();
        for (Territory territory : riscMap.getTerritoryList()) {
            idSet.add(territory.getOwnerID());
        }
        if (idSet.size() <= 2) {
            allianceButton.setDisable(true);
        }
    }

    public MapGraphic getMapGraphic() {
        return mapGraphic;
    }

    public Button getMoveButton() {
        return moveButton;
    }

    public Button getAttackButton() {
        return attackButton;
    }

    public Button getUpgradeButton() {
        return upgradeButton;
    }

    public Button getAllianceButton() {
        return allianceButton;
    }

    public Button getChatButton() {
        return chatButton;
    }

    public Button getSubmitOrderButton() {
        return submitOrderButton;
    }

    public String getText() {
        String text = chatInput.getText();
        chatInput.clear();
        return text;
    }

    public void appendText(String text) {
        chatOutput.appendText(text);
    }

    private Pane makePlayerInfoPanel() {
        chatOutput = new TextArea();
        chatInput = new TextField();
        chatOutput.setEditable(false);
        chatOutput.setWrapText(true);
        chatOutput.prefHeightProperty().bind(mapPane.heightProperty().divide(2));
        chatButton = new Button("Send");
        BorderPane chatbox = new BorderPane();
        chatbox.setTop(chatOutput);
        chatbox.setCenter(chatInput);
        chatbox.setRight(chatButton);

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        playerIDLabel = new Label("Player ID = \n");
        playerTechLabel = new Label(": ",
                new ImageView(new Image("icon-tech-resource.png", 50, 50, true, true)));
        playerFoodLabel = new Label(": ",
                new ImageView(new Image("icon-food-resource.png", 50, 50, true, true)));

        playerTechLabel.setTooltip(new Tooltip("Player Tech Resource"));
        playerFoodLabel.setTooltip(new Tooltip("Player Food Resource"));

        Rectangle colorSample = new Rectangle(30, 30);
        playerColorLabel = new Label(" Player color", colorSample);
        colorSample.setFill(Color.TRANSPARENT);
        vBox.getChildren().add(playerIDLabel);
        vBox.getChildren().add(playerTechLabel);
        vBox.getChildren().add(playerFoodLabel);
        vBox.getChildren().add(playerColorLabel);

        submitOrderButton = new Button("Submit Orders");

        VBox playerInfoPanel = new VBox(30);
        playerInfoPanel.setAlignment(Pos.TOP_CENTER);
        playerInfoPanel.getChildren().addAll(chatbox, vBox, submitOrderButton);
        playerInfoPanel.prefWidthProperty().bind(mapPane.widthProperty().divide(4));

        //playerInfoPanel.setBackground(new Background(new BackgroundImage(new Image("physical-world-of-europe.jpg"), null, null, null, null)));
        return playerInfoPanel;
    }

    private Pane makeTerritoryInfoPanel() {
        HBox territoryInfoPanel = new HBox(50);
        territoryInfoPanel.setAlignment(Pos.CENTER);

        techResourceLabel = new Label(":",
                new ImageView(new Image("icon-tech-resource.png", 50, 50, true, true)));
        foodResourceLabel = new Label(":",
                new ImageView(new Image("icon-food-resource.png", 50, 50, true, true)));
        techResourceLabel.setTooltip(new Tooltip("Tech Resource Production"));
        foodResourceLabel.setTooltip(new Tooltip("Food Resource Production"));

        GridPane resourcePanel = new GridPane();
        resourcePanel.setAlignment(Pos.CENTER);
        resourcePanel.add(techResourceLabel, 0, 0);
        resourcePanel.add(foodResourceLabel, 0, 1);
        resourcePanel.setHgap(20);
        resourcePanel.setVgap(20);

        unitsPanel = new GridPane();
        unitsPanel.setAlignment(Pos.CENTER);
        unitsPanel.add(new Label("Level"), 0, 0);
        for (int i = 0; i < 7; i++) {
            /*
            unitsPanel.add(new Label(String.format("Level %d units = \n", i)),
                    i / 4, i % 4);
             */
            unitsPanel.add(new Label(String.format("%d", i)), i + 1, 0);
        }
        unitsPanel.setHgap(20);

        territoryNamePanel = new VBox(20);
        territoryNamePanel.setAlignment(Pos.CENTER);
        territoryNamePanel.getChildren().add(new Label("Territory name"));
        territoryNamePanel.getChildren().add(new Label("Owner: "));
        territoryNamePanel.getChildren().add(new Label("Size: "));

        HBox actionButtonBox = new HBox(20);
        actionButtonBox.setAlignment(Pos.CENTER);
        moveButton = new Button("Move");
        attackButton = new Button("Attack");
        upgradeButton = new Button("Upgrade");
        allianceButton = new Button("Alliance");
        actionButtonBox.getChildren().addAll(
                moveButton, attackButton, upgradeButton, allianceButton);

        territoryInfoPanel.getChildren().addAll(
                resourcePanel, unitsPanel, territoryNamePanel, actionButtonBox);
        return territoryInfoPanel;
    }

    public void setTerritoryInfo(Territory territory, int territoryID) {
        techResourceLabel.setText(": " + territory.getTechResourceProduction());
        foodResourceLabel.setText(": " + territory.getFoodResourceProduction());

        unitsPanel.getChildren().clear();
        Set<Integer> playerIDSet = new TreeSet<>();
        for (Territory allTerritory : riscMap.getTerritoryList()) {
            playerIDSet.add(allTerritory.getOwnerID());
        }

        unitsPanel.add(new Label("Level"), 0, 0);
        for (int i = 0; i < 7; i++) {
            unitsPanel.add(new Label(String.format("%d", i)), i + 1, 0);
            int j = 1;
            for (int id : playerIDSet) {
                unitsPanel.add(new Label(String.format("%d", id)), 0, j);
                unitsPanel.add(new Label(String.format("%d",
                        territory.numOfUnitsOfTypeOfOwner(i, id))), i + 1, j);
                j++;
            }
        }

        territoryNamePanel.getChildren().clear();
        territoryNamePanel.getChildren().add(new Label("" + territoryID));
        territoryNamePanel.getChildren().add(new Label("Owner: " + territory.getOwnerID()));
        territoryNamePanel.getChildren().add(new Label("Size: " + territory.getTerritorySize()));
    }

    public void setPlayerInfo(RiscPlayerInRoom playerInRoom) {
        playerIDLabel.setText("Player ID = " + playerInRoom.getPlayerID());
        playerTechLabel.setText(": " + playerInRoom.getTechnologyResource().size());
        playerFoodLabel.setText(": " + playerInRoom.getFoodResource().size());
        Rectangle colorSample = new Rectangle(30, 30);
        colorSample.setFill(MapGraphic.getColor(playerInRoom.getColorID()));
        playerColorLabel.setGraphic(colorSample);
    }

    public Dialog<Instruction> actionInputPopupWindow(String actionType) {
        Dialog<Instruction> dialog = new Dialog<>();
        dialog.setTitle(String.format("%s Action", actionType));
        dialog.setHeaderText(String.format("%s Action", actionType));

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> endTerritory = new ComboBox<>();
        ComboBox<String> allianceID = new ComboBox<>();
        Set<String> allianceIDSet = new TreeSet<>();
        for (Territory territory : riscMap.getTerritoryList()) {
            endTerritory.getItems().add(
                    String.format("%d", territory.getTerritoryID()));
            allianceIDSet.add(String.format("%d", territory.getOwnerID()));
        }
        allianceID.getItems().addAll(allianceIDSet);
        endTerritory.getSelectionModel().selectFirst();
        allianceID.getSelectionModel().selectFirst();

        ComboBox<String> fromUnitType = new ComboBox<>();
        ComboBox<String> toUnitType = new ComboBox<>();
        for (int type : riscMap.getTerritoryList().get(0).getUnitTypes()) {
            fromUnitType.getItems().add(String.format("%d", type));
            toUnitType.getItems().add(String.format("%d", type));
        }
        fromUnitType.getSelectionModel().selectFirst();
        toUnitType.getSelectionModel().selectFirst();
        TextField unitNumber = new TextField();
        unitNumber.setPromptText("Number of units");


        /*
        TextField endTerritory = new TextField();
        endTerritory.setPromptText("Target territory number");
        TextField fromUnitType = new TextField();
        fromUnitType.setPromptText("Which unit Type");
        TextField toUnitType = new TextField();
        toUnitType.setPromptText("To which unit type");
         */

        if (actionType.toLowerCase().equals("alliance")) {
            grid.add(new Label("Alliance ID:"), 0, 0);
            grid.add(allianceID, 1, 0);
        } else {
            if (!actionType.toLowerCase().equals("upgrade")) {
                grid.add(new Label("Target Territory:"), 0, 0);
                grid.add(endTerritory, 1, 0);
            } else {
                grid.add(new Label("New Unit Type:"), 0, 2);
                grid.add(toUnitType, 1, 2);
            }
            grid.add(new Label("Unit Type:"), 0, 1);
            grid.add(fromUnitType, 1, 1);
            grid.add(new Label("Number of Units:"), 0, 3);
            grid.add(unitNumber, 1, 3);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return new Instruction(actionType.toLowerCase(),
                        String.format("%d", selectedTerritoryID),
                        endTerritory.getValue(),
                        unitNumber.getText().equals("") ? "0" : unitNumber.getText(),
                        fromUnitType.getValue(),
                        toUnitType.getValue(),
                        allianceID.getValue());
            }
            return null;
        });
        return dialog;
    }

    public Territory getSelectedTerritory() {
        return selectedTerritory;
    }

    public void setSelectedTerritory(Territory selectedTerritory) {
        this.selectedTerritory = selectedTerritory;
    }

    public void setSelectedTerritoryID(int selectedTerritoryID) {
        this.selectedTerritoryID = selectedTerritoryID;
    }
}