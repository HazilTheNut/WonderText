package Game;

import Data.LayerImportances;
import Engine.Layer;
import Engine.LayerManager;
import Engine.SpecialText;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jared on 3/29/2018.
 */
class PlayerInventory implements MouseInputReceiver{

    private Player player;
    private ArrayList<Item> items = new ArrayList<>();

    private Layer invLayer;
    private Layer selectorLayer;

    private final int ITEM_STRING_LENGTH = 16;

    public void addItem(Item item) { items.add(item); }

    PlayerInventory(LayerManager lm){
        invLayer = new Layer(new SpecialText[ITEM_STRING_LENGTH + 2][100], "inventory", 0, 1, LayerImportances.MENU);
        invLayer.fixedScreenPos = true;
        invLayer.setVisible(false);
        selectorLayer = new Layer(new SpecialText[ITEM_STRING_LENGTH + 2][1],   "inventory_selector", 0, 1, LayerImportances.MENU_CURSOR);
        selectorLayer.fillLayer(new SpecialText(' ', Color.WHITE, new Color(200, 200, 200, 100)));
        selectorLayer.setVisible(false);
        selectorLayer.fixedScreenPos = true;
        lm.addLayer(invLayer);
        lm.addLayer(selectorLayer);
    }

    private int getInvHeight() {
        return items.size() + 1;
    }

    void show(){
        updateDisplay();
        invLayer.setVisible(true);
    }

    public void updateDisplay(){
        System.out.printf("[PlayerInventory] Size: %1$d\n", items.size());
        int height = getInvHeight();
        for (int row = 0; row < height; row++){ //Draw base inv panel
            for (int col = 0; col < invLayer.getCols(); col++){
                invLayer.editLayer(col, row, new SpecialText(' ', Color.WHITE, new Color(35, 35, 35)));
            }
        }
        for (int col = 0; col < invLayer.getCols(); col++){ //Create top border
            invLayer.editLayer(col, 0,        new SpecialText('#', Color.GRAY, new Color(30, 30, 30)));
        }
        for (int ii = 0; ii < items.size(); ii++){ //Inscribe inv contents
            if (ii % 2 == 1){
                for (int col = 0; col < ITEM_STRING_LENGTH   ; col++){
                    invLayer.editLayer(col, ii+1, new SpecialText(' ', Color.WHITE, new Color(45, 45, 45)));
                }
            }
            invLayer.inscribeString(items.get(ii).getItemData().getName(), 0, ii+1);
            invLayer.inscribeString(String.format("%1$02d", items.get(ii).getItemData().getQty()), 16, ii+1, new Color(240, 255, 200));
        }
        invLayer.inscribeString("Inventory", 1, 0, new Color(240, 255, 200));
    }

    void close(){
        invLayer.setVisible(false);
        selectorLayer.setVisible(false);
    }

    boolean isShowing() { return invLayer.getVisible(); }

    private boolean cursorInInvLayer(Coordinate screenPos){
        Coordinate adjustedPosition = new Coordinate(screenPos.getX() - invLayer.getX(), screenPos.getY() - invLayer.getY());
        return !invLayer.isLayerLocInvalid(adjustedPosition);
    }

    private Item getItemAtCursor(Coordinate screenPos){
        if (cursorInInvLayer(screenPos) && invLayer.getVisible()){
            int index = screenPos.getY() - invLayer.getY() - 1;
            if (index >= 0 && index < items.size()){
                return items.get(index);
            }
        }
        return null;
    }

    @Override
    public void onMouseMove(Coordinate levelPos, Coordinate screenPos) {
        if (getItemAtCursor(screenPos) != null){
            selectorLayer.setVisible(true);
            selectorLayer.setPos(invLayer.getX(), screenPos.getY());
        } else {
            selectorLayer.setVisible(false);
        }
    }

    @Override
    public boolean onMouseClick(Coordinate levelPos, Coordinate screenPos) {
        Item selectedItem = getItemAtCursor(screenPos);
        if (selectedItem != null){
            selectedItem.onItemUse();
        }
        return invLayer.getVisible() && cursorInInvLayer(screenPos);
    }
}