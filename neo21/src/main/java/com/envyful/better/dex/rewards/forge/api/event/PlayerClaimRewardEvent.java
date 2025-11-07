package com.envyful.better.dex.rewards.forge.api.event;

import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 *
 * Fired when the player claims a dex completion reward in the UI.
 * Fired on the {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS}
 *
 */
public class PlayerClaimRewardEvent extends Event {

    private final ServerPlayer player;
    private final DexCompletion reward;

    public PlayerClaimRewardEvent(ServerPlayer player, DexCompletion reward) {
        this.player = player;
        this.reward = reward;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public DexCompletion getReward() {
        return this.reward;
    }
}
