package Editor.DrawTools;

import Engine.Layer;
import Engine.LayerManager;
import Engine.SpecialText;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jared on 2/25/2018.
 */
public class ArtRectangle extends DrawTool {

    private int startX;
    private int startY;

    private int previousX;
    private int previousY;

    private SpecialText startHighlight = new SpecialText(' ', Color.WHITE, new Color(75, 75, 255, 120));
    private LayerManager lm;

    private JCheckBox fillBox;

    public ArtRectangle(LayerManager manager) {lm = manager; }

    @Override
    public void onActivate(JPanel panel) {
        fillBox = new JCheckBox("Filled", false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(fillBox);
        panel.setBorder(BorderFactory.createTitledBorder("Rectangle Tool"));
        panel.setVisible(true);
        panel.validate();

        TOOL_TYPE = TYPE_ART;
    }

    @Override
    public void onDrawStart(Layer layer, Layer highlight, int col, int row, SpecialText text) {
        startX = col;
        startY = row;
    }

    @Override
    public void onDraw(Layer layer, Layer highlight, int col, int row, SpecialText text) {
        int xOffset = (int)lm.getCameraPos().getX() + layer.getX();
        int yOffset = (int)lm.getCameraPos().getY() + layer.getY();
        drawRect(highlight, startX + xOffset, startY + yOffset, previousX + xOffset, previousY + yOffset, null);
        drawRect(highlight, startX + xOffset, startY + yOffset, col + xOffset, row + yOffset, startHighlight);
        previousX = col;
        previousY = row;
        //System.out.println("[ArtLine] onDraw");
    }

    @Override
    public void onDrawEnd(Layer layer, Layer highlight, int col, int row, SpecialText text) {
        int xOffset = (int)lm.getCameraPos().getX() + layer.getX();
        int yOffset = (int)lm.getCameraPos().getY() + layer.getY();
        drawRect(highlight, startX + xOffset, startY + yOffset, col + xOffset, row + yOffset, null);
        drawRect(layer, startX, startY, col, row, text);
    }

    private void drawRect(Layer layer, int x1, int y1, int x2, int y2, SpecialText text){
        if (fillBox.isSelected()){
            for (int col = x1; col <= x2; col++){
                for (int row = y1; row <= y2; row++){
                    layer.editLayer(col, row, text);
                }
            }
        } else {
            for (int col = x1; col <= x2; col++) {
                layer.editLayer(col, y1, text);
                layer.editLayer(col, y2, text);
            }
            for (int row = y1; row <= y2; row++) {
                layer.editLayer(x1, row, text);
                layer.editLayer(x2, row, text);
            }
        }
    }
}
