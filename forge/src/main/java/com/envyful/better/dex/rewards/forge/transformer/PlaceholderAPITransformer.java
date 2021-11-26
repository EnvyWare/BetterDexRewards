package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.envyful.papi.api.util.UtilPlaceholder;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlaceholderAPITransformer implements Transformer {

    private final EntityPlayerMP player;

    public static PlaceholderAPITransformer of(EntityPlayerMP player) {
        return new PlaceholderAPITransformer(player);
    }

    private PlaceholderAPITransformer(EntityPlayerMP player) {this.player = player;}

    @Override
    public String transformName(String name) {
        return UtilPlaceholder.replaceIdentifiers(this.player, name);
    }
}
