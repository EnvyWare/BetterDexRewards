package com.envyful.better.dex.rewards.forge.api.event;

import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.envyful.better.dex.rewards.forge.config.DexRewardBonus;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 *
 * Fired when the player is paid a stage's bonus, once per bonus: with the stage claim (after
 * {@link PlayerClaimRewardEvent}), or on its own when the bonus was met after the stage was claimed.
 * Posted just before the bonus rewards are given. Not cancellable.
 * Fired on the {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS}
 *
 */
public class PlayerClaimBonusEvent extends Event {

    private final ServerPlayer player;
    private final DexCompletion stage;
    private final DexRewardBonus bonus;

    public PlayerClaimBonusEvent(ServerPlayer player, DexCompletion stage, DexRewardBonus bonus) {
        this.player = player;
        this.stage = stage;
        this.bonus = bonus;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public DexCompletion getStage() {
        return this.stage;
    }

    public DexRewardBonus getBonus() {
        return this.bonus;
    }
}
