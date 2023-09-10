package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.text.parse.SimplePlaceholder;
import com.envyful.papi.api.util.UtilPlaceholder;
import net.minecraft.server.level.ServerPlayer;

public class PlaceholderAPITransformer implements SimplePlaceholder {

    private final ServerPlayer player;

    public static PlaceholderAPITransformer of(ServerPlayer player) {
        return new PlaceholderAPITransformer(player);
    }

    private PlaceholderAPITransformer(ServerPlayer player) {this.player = player;}

    @Override
    public String replace(String name) {
        return UtilPlaceholder.replaceIdentifiers(this.player, name);
    }
}
