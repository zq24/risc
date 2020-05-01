package ece651.risc.team11;

import ece651.risc.team11.GUIConsole.GameSceneCreator;
import org.junit.jupiter.api.Test;

public class GameSeceCreatorTest {
    @Test
    void gameSecenTest() {
        GameSceneCreator gameSceneCreator = new GameSceneCreator();
        gameSceneCreator.getAllianceButton();
        gameSceneCreator.getAttackButton();
        gameSceneCreator.getChatButton();
        gameSceneCreator.getMapGraphic();
        gameSceneCreator.getMoveButton();
        gameSceneCreator.getSelectedTerritory();
        gameSceneCreator.getSubmitOrderButton();
        //gameSceneCreator.getText();
        gameSceneCreator.getUpgradeButton();
        // gameSceneCreator.setTerritoryInfo(new NormalTerritory(), 0);
    }
}