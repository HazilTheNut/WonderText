package Game.Tags.EnchantmentTags;

import Data.SerializationVersion;
import Game.Entities.Entity;
import Game.Registries.TagRegistry;
import Game.TagEvent;

import java.awt.*;

/**
 * Created by Jared on 4/15/2018.
 */
public class BleedEnchantmentTag extends EnchantmentTag {

    private static final long serialVersionUID = SerializationVersion.SERIALIZATION_VERSION;

    @Override
    public void onContact(TagEvent e) {
        if (e.getTarget() instanceof Entity && e.getTarget().hasTag(TagRegistry.LIVING)) {
            e.getTarget().addTag(TagRegistry.BLEEDING, e.getSource());
        }
    }

    @Override
    public Color getTagColor() {
        return new Color(191, 0, 13);
    }
}
