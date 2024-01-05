package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.text.parse.SimplePlaceholder;
import com.envyful.papi.api.util.UtilPlaceholder;
import net.minecraft.entity.player.ServerPlayerEntity;

public class PlaceholderAPITransformer implements SimplePlaceholder {

    private final ServerPlayerEntity player;

    public static PlaceholderAPITransformer of(ServerPlayerEntity player) {
        return new PlaceholderAPITransformer(player);
    }

    private PlaceholderAPITransformer(ServerPlayerEntity player) {this.player = player;}

    @Override
    public String replace(String name) {
        return UtilPlaceholder.replaceIdentifiers(this.player, name);
    }
}
