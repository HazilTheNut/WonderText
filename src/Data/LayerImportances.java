package Data;

/**
 * Created by Jared on 3/28/2018.
 */
public class LayerImportances {

    /*
     LayerImportances

     A static utility object that defines the importances of different types of layer

     Because all relevant layers source their importance number from this stuff, you can effectively shift around the importance of whole groups of layers.
     That's nice when you just want to make sure a certain layers squeezes in between two other groups

     Things are spaced into into 5's and such to conveniently allow for in-between importances

     */

    public static final int BACKDROP          = 0;
    public static final int TILE_OVERLAY      = 10;
    public static final int TILE_TAG          = 13;
    public static final int TILE_ANIM         = 15;
    public static final int ENTITY            = 20;
    public static final int ENTITY_SOLID      = 25;
    public static final int ANIMATION         = 30;
    public static final int VFX               = 35;
    public static final int GAME_CURSOR       = 40;
    public static final int HUD_SYNOPSIS      = 45;
    public static final int MENU              = 50;
    public static final int MENU_CURSOR       = 55;
    public static final int QUICKMENU         = 57;
    public static final int QUICKMENU_CURSOR  = 58;
    public static final int LOADING_SCREEN    = 59;
    public static final int HUD               = 60;
    public static final int HUD_SPELL_MENU    = 65;
    public static final int HUD_SPELL_CURSOR  = 67;
    public static final int TEXT_BOX          = 70;
    public static final int MAIN_MENU         = 80;
    public static final int MAIN_MENU_CURSOR  = 85;
    public static final int MENU_SUPER        = 90;
    public static final int MENU_SUPER_CURSOR = 95;

    //For use in Editor

    public static final int EDITOR_ART        = 0;
    public static final int EDITOR_TILE       = 2;
    public static final int EDITOR_ENTITY     = 4;
    public static final int EDITOR_WARPZONE   = 6;
    public static final int EDITOR_SCRIPTMASK = 8;
}
