package Game.Spells;

import Data.Coordinate;
import Game.Debug.DebugWindow;
import Game.Entities.Entity;
import Game.GameInstance;

/**
 * Created by Jared on 4/19/2018.
 */
public class Spell {

    /**
     * Perfroms spell cast
     * @param targetLoc Target location for spell (aiming, etc.)
     * @param spellCaster The one doing the spell casting
     * @param gi The Game Instance
     * @return The spell's cooldown
     */
    public int castSpell(Coordinate targetLoc, Entity spellCaster, GameInstance gi, int magicPower){ return 0; }

    public String getName(){ return "~";}

    int calculateCooldown(int baseCooldown, int magicPower){
        int result = (int)(baseCooldown * ((100 - (float)magicPower) / 100));
        DebugWindow.reportf(DebugWindow.MISC, "Spell.calculateCooldown","Base: %1$d ; Final: %2$d (-%3$d%%)", baseCooldown, result, magicPower);

        return result;
    }

    int calculateDamage(int baseDamage, int magicPower){
        int result = baseDamage + (int)(baseDamage * ((float)magicPower * 3f / 100));
        DebugWindow.reportf(DebugWindow.MISC, "Spell.calculateDamage","Base: %1$d ; Final: %2$d (+%3$d%%)", baseDamage, result, magicPower * 3);
        return result;
    }

}
