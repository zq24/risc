package ece651.risc.team11;

import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class MapGraphic {
    private WritableImage territoryBorder;
    private static final int size = 1000;
    private static final int[][] idArray = new int[size][size];
    private final ArrayList<ImageView> territoryImageBlocks;
    static ArrayList<Color> colorList = new ArrayList<>(
            Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.PURPLE, Color.AQUA));

    public MapGraphic() {
        territoryImageBlocks = new ArrayList<>();
    }

    public Pane createMap(RiscMap riscMap) {
        Pane pane = new Pane();
        ArrayList<WritableImage> territoryBlocks = new ArrayList<>();
        for (int i = 0; i < riscMap.getNumberOfTerritories(); i++) {
            territoryBlocks.add(new WritableImage(size, size));
        }
        drawMap(riscMap, territoryBlocks);
        drawBorder();

        territoryImageBlocks.clear();
        for (WritableImage writableImage : territoryBlocks) {
            ImageView colorBlocksImage = new ImageView(writableImage);
            territoryImageBlocks.add(colorBlocksImage);
        }
        for (int i = 0; i < territoryImageBlocks.size(); i++) {
            ImageView colorBlocksImage = territoryImageBlocks.get(i);
            colorBlocksImage.setOpacity(0.7);
            colorBlocksImage.fitWidthProperty().bind(pane.widthProperty());
            colorBlocksImage.fitHeightProperty().bind(pane.heightProperty());
            Set<Integer> neighborSet = riscMap.getTerritoryAdjList().get(i);

            Tooltip.install(colorBlocksImage,
                    makeTooltip(riscMap.getTerritoryList().get(i), i));

            colorBlocksImage.setOnMouseEntered(mouseEvent -> {
                /*
                colorBlocksImage.setEffect(new DropShadow());
                for (int neighbor : neighborSet) {
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(0.5);
                    territoryImageBlocks.get(neighbor).setEffect(colorAdjust);
                }
                */
                colorBlocksImage.setOpacity(0.9);
                for (int neighbor : neighborSet) {
                    //territoryImageBlocks.get(neighbor).setOpacity(0.3);
                }
            });
            colorBlocksImage.setOnMouseExited(mouseEvent -> {
                /*
                colorBlocksImage.setEffect(null);
                for (int neighbor : neighborSet) {
                    territoryImageBlocks.get(neighbor).setEffect(null);
                }
                 */
                colorBlocksImage.setOpacity(0.7);
                for (int neighbor : neighborSet) {
                    territoryImageBlocks.get(neighbor).setOpacity(0.7);
                }
            });

            pane.getChildren().add(colorBlocksImage);
        }
        ImageView territoryBorderImage = new ImageView(territoryBorder);
        territoryBorderImage.fitWidthProperty().bind(pane.widthProperty());
        territoryBorderImage.fitHeightProperty().bind(pane.heightProperty());

        pane.getChildren().add(territoryBorderImage);
        return pane;
    }

    public ArrayList<ImageView> getTerritoryImageBlocks() {
        return territoryImageBlocks;
    }

    private void drawMap(RiscMap riscMap, ArrayList<WritableImage> territoryBlocks) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int n = 0;
                for (int i = 0; i < riscMap.getNumberOfTerritories(); i++) {
                    if (distance((int) riscMap.getTerritoryList().get(i).getX(), x,
                            (int) riscMap.getTerritoryList().get(i).getY(), y)
                            < distance((int) riscMap.getTerritoryList().get(n).getX(), x,
                            (int) riscMap.getTerritoryList().get(n).getY(), y)) {
                        n = i;
                    }
                }
                idArray[x][y] = n;
                territoryBlocks.get(n).getPixelWriter().setColor(x, y, colorList.get(riscMap.getTerritoryList().get(n).getColor()));
            }
        }
    }

    private void drawBorder() {
        territoryBorder = new WritableImage(size, size);
        PixelWriter pixelWriter = territoryBorder.getPixelWriter();
        for (int x = 1; x < size; x++) {
            for (int y = 1; y < size; y++) {
                if (idArray[x][y - 1] != idArray[x][y]) {
                    pixelWriter.setColor(x, y - 1, Color.BLACK);
                }
                if (idArray[x - 1][y] != idArray[x][y]) {
                    pixelWriter.setColor(x - 1, y, Color.BLACK);
                }
                if (idArray[x - 1][y - 1] != idArray[x][y]) {
                    pixelWriter.setColor(x - 1, y - 1, Color.BLACK);
                }

            }
        }
    }

    private Tooltip makeTooltip(Territory territory, int territoryID) {
        Tooltip tooltip = new Tooltip();
        tooltip.setFont(Font.font(16));
        StringBuilder tooltipText = new StringBuilder(
                String.format("Territory ID = %d\n", territoryID) +
                        String.format("Owner = %d\n", territory.getOwnerID()) +
                        String.format("Territory Size = %d\n", Territory.getTerritorySize()) +
                        String.format("Tech Resource Production = %d\n", territory.getTechResourceProduction()) +
                        String.format("Food Resource Production = %d\n", territory.getFoodResourceProduction())
        );
        for (Integer i : territory.getUnitTypes()) {
            tooltipText.append(String.format("Number of Level %d units = %d\n",
                    i, territory.getUnitsForType(i).size()));
        }

        tooltip.setText(tooltipText.toString());
        tooltip.setShowDelay(Duration.seconds(0.5));
        return tooltip;
    }

    public static Color getColor(int i) {
        return colorList.get(i);
    }

    static double distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}