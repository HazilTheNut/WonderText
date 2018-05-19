package Game;

import Data.Coordinate;
import Data.LayerImportances;
import Engine.Layer;
import Engine.LayerManager;
import Engine.SpecialText;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jared on 5/14/2018.
 */
public class QuickMenu implements MouseInputReceiver {

    private Layer menuLayer;
    private Layer selectorLayer;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private Player player;
    private LayerManager lm;

    private final int MENU_WIDTH = 17;
    private final SpecialText closeButtonDull = new SpecialText('x', new Color(224, 79, 79), new Color(55, 10, 10));
    private final SpecialText closeButtonLit  = new SpecialText('x', new Color(217, 130, 130), new Color(102, 20, 20));
    private boolean isClosable = true;

    public QuickMenu(LayerManager lm, Player player){
        menuLayer = new Layer(MENU_WIDTH, 10, "quick_menu", 0, 0, LayerImportances.QUICKMENU);
        menuLayer.fixedScreenPos = true;
        menuLayer.setVisible(false);
        lm.addLayer(menuLayer);

        selectorLayer = new Layer(MENU_WIDTH, 1, "quick_menu_cursor", 0, 0, LayerImportances.QUICKMENU_CURSOR);
        selectorLayer.fixedScreenPos = true;
        selectorLayer.setVisible(false);
        selectorLayer.fillLayer(new SpecialText(' ', Color.WHITE, new Color(200, 200, 200, 100)));
        lm.addLayer(selectorLayer);

        this.player = player;
        this.lm = lm;
    }

    public void clearMenu(){
        menuItems.clear();
    }

    public void addMenuItem(String name, Color color, MenuAction action){
        menuItems.add(new MenuItem(name, color, action));
    }

    public void addMenuItem(String name, MenuAction action){
        addMenuItem(name, Color.WHITE, action);
    }

    public void showMenu(String title, boolean closable){
        isClosable = closable;
        drawMenu();
        Coordinate playerScreenPos = player.getLocation().subtract(lm.getCameraPos());
        int x = playerScreenPos.getX() - MENU_WIDTH - 3;
        if (x < 0) x = playerScreenPos.getX() + 3;
        int y = playerScreenPos.getY() - menuLayer.getRows() / 2;
        y = Math.max(1, Math.min(y, lm.getWindow().RESOLUTION_HEIGHT - menuLayer.getRows()));
        menuLayer.setPos(x, y);
        menuLayer.inscribeString(title, (int)Math.floor((MENU_WIDTH - (double)title.length())/2), 0, new Color(220, 255, 255));
        menuLayer.setVisible(true);
        player.freeze();
    }

    public void closeMenu(){
        menuLayer.setVisible(false);
        selectorLayer.setVisible(false);
        player.unfreeze();
    }

    private void drawMenu(){
        Layer tempLayer = new Layer(menuLayer.getCols(), menuItems.size() + 1, "qm temp", 0, 0, 0);
        tempLayer.fillLayer(new SpecialText(' ', Color.WHITE, new Color(29, 29, 29)));
        for (int col = 0; col < tempLayer.getCols(); col++) {
            tempLayer.editLayer(col, 0, new SpecialText('#', new Color(42, 42, 42), new Color(34, 34, 34)));
        }
        for (int i = 0; i < menuItems.size(); i++) {
            tempLayer.inscribeString(menuItems.get(i).name, 1, i+1);
        }
        if (isClosable) tempLayer.editLayer(MENU_WIDTH-1, 0, closeButtonDull);
        menuLayer.transpose(tempLayer);
    }

    private Coordinate prevPos = new Coordinate(0, 0);

    @Override
    public boolean onMouseMove(Coordinate levelPos, Coordinate screenPos) {
        if (menuLayer.getVisible() && !prevPos.equals(screenPos)) {
            Coordinate layerPos = screenPos.subtract(menuLayer.getPos());
            if (!menuLayer.isLayerLocInvalid(layerPos)){
                if (layerPos.getY() > 0) {
                    selectorLayer.setVisible(true);
                    selectorLayer.setPos(menuLayer.getX(), layerPos.getY() + menuLayer.getY());
                } else {
                    selectorLayer.setVisible(false);
                }
            }
            if (isClosable) {
                if (layerPos.equals(new Coordinate(MENU_WIDTH-1, 0)))
                    menuLayer.editLayer(MENU_WIDTH - 1, 0, closeButtonLit);
                else
                    menuLayer.editLayer(MENU_WIDTH - 1, 0, closeButtonDull);
            }
            prevPos = screenPos;
        }
        return menuLayer.getVisible();
    }

    @Override
    public boolean onMouseClick(Coordinate levelPos, Coordinate screenPos, int mouseButton) {
        if (menuLayer.getVisible()) {
            Coordinate layerPos = screenPos.subtract(menuLayer.getPos());
            if (!menuLayer.isLayerLocInvalid(layerPos) && layerPos.getY() > 0) {
                menuItems.get(layerPos.getY() - 1).action.doAction();
                closeMenu();
                return true;
            } else if (layerPos.equals(new Coordinate(MENU_WIDTH-1, 0)) && isClosable){
                closeMenu();
                return true;
            }
        }
        return menuLayer.getVisible();
    }

    @Override
    public boolean onMouseWheel(Coordinate levelPos, Coordinate screenPos, double wheelMovement) {
        return false;
    }

    public interface MenuAction {
        void doAction();
    }

    private class MenuItem{
        MenuAction action;
        String name;
        Color color;
        private MenuItem(String name, Color color, MenuAction action){
            this.name = name;
            this.color = color;
            this.action = action;
        }
    }
}