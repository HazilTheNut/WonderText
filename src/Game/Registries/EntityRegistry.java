package Game.Registries;

import Data.EntityStruct;
import Engine.SpecialText;
import Game.Entities.*;
import Game.Entities.PuzzleElements.FloorSwitch;
import Game.Entities.PuzzleElements.MovableCrate;
import Game.Entities.PuzzleElements.PoweredDoor;
import Game.PlayerShadow;

import java.awt.*;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Jared on 3/3/2018.
 */
public class EntityRegistry {

    private static TreeMap<Integer, EntityStruct> entityStructMap = new TreeMap<>();
    private static TreeMap<Integer, Class> entityObjMap = new TreeMap<>();

    public static final int LOOT_PILE = 7;
    public static final int PLAYER_SHADOW = 10;

    static {
        //Registering stuff starts here

        registerEntity(0,  "Dummy",        new SpecialText('D'),                                                         Dummy.class, TagRegistry.FLAMMABLE, TagRegistry.NO_PATHING, TagRegistry.LIVING);
        registerEntity(1,  "Save Point",   new SpecialText('S', new Color(40, 225, 115), new Color(20, 100, 80, 40)),    SavePoint.class, TagRegistry.NO_PATHING, TagRegistry.IMMOVABLE);
        registerEntity(2,  "Basic Enemy",  new SpecialText('E', new Color(255, 130, 130), new Color(255, 180, 180, 15)), BasicEnemy.class, TagRegistry.FLAMMABLE, TagRegistry.LIVING);
        registerEntity(3,  "Sign",         new SpecialText('S', new Color(110, 100, 250), new Color(55, 50, 125, 30)),   Sign.class, TagRegistry.NO_PATHING);
        registerEntity(4,  "Chest",        new SpecialText('C', new Color(245, 245, 175), new Color(175, 100,  35, 45)), Chest.class, TagRegistry.NO_PATHING);
        registerEntity(5,  "Door",         new SpecialText('-', new Color(143, 74, 17),   new Color(75, 45, 10, 50)),    Door.class, TagRegistry.NO_PATHING, TagRegistry.IMMOVABLE);
        registerEntity(6,  "Locked Door",  new SpecialText('-', new Color(143, 123, 107), new Color(74, 65, 55, 50)),    LockedDoor.class, TagRegistry.NO_PATHING, TagRegistry.IMMOVABLE);
        registerEntity(LOOT_PILE, "Loot",  new SpecialText('%', new Color(191, 191, 75),  new Color(155, 155, 60, 15)),  LootPile.class);
        registerEntity(8,  "One-Way Door", new SpecialText('x', new Color(143, 123, 107), new Color(74, 65, 55, 50)),    OneWayDoor.class, TagRegistry.NO_PATHING, TagRegistry.IMMOVABLE);
        registerEntity(9,  "Magnet",       new SpecialText('M', new Color(145, 145, 145), new Color(45, 45, 45, 45)),    Magnet.class, TagRegistry.METALLIC);
        registerEntity(PLAYER_SHADOW, "Player Shadow", new SpecialText('@', new Color(65, 75, 65), new Color(0, 0, 0, 25)), PlayerShadow.class);

        registerEntity(11, "Floor Switch", new SpecialText('o', new Color(165, 165, 135), new Color(70, 70, 50, 75)),    FloorSwitch.class, TagRegistry.IMMOVABLE);
        registerEntity(12, "Powered Door", new SpecialText('-', new Color(143, 123, 80),  new Color(74, 65, 55, 75)),    PoweredDoor.class, TagRegistry.IMMOVABLE);
        registerEntity(13, "Large Crate",  new SpecialText('#', new Color(120, 75, 34),   new Color(56, 32, 12, 100)),   MovableCrate.class, TagRegistry.FLAMMABLE, TagRegistry.NO_PATHING);

        //Registering stuff ends here
    }

    public static int[] getMapKeys() {
        Set<Integer> ints = entityStructMap.keySet();
        int[] output = new int[ints.size()];
        int index = 0;
        for (int i : ints){
            output[index] = i;
            index++;
        }
        return output;
    }

    public static EntityStruct getEntityStruct (int id) { return entityStructMap.get(id).copy(); }

    public static Class getEntityClass (int id) { return entityObjMap.get(id); }

    private static void registerEntity(int id, String name, SpecialText text, Class entityClass, int... tags){
        entityStructMap.put(id, new EntityStruct(id, name, text, tags));
        if (entityClass != null)
            entityObjMap.put(id, entityClass);
        else
            entityObjMap.put(id, null);
    }
}
