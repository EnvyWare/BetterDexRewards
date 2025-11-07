package com.envyful.better.dex.rewards.forge.api.event;

import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 *
 * Fired when the player ranks up in their dex completion.
 * Fired on the {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS}
 *
 */
public class PlayerRankupEvent extends Event {

    private final ServerPlayer player;
    private final DexCompletion newRank;

    public PlayerRankupEvent(ServerPlayer player, DexCompletion newRank) {
        this.player = player;
        this.newRank = newRank;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public DexCompletion getNewRank() {
        return this.newRank;
    }
}
