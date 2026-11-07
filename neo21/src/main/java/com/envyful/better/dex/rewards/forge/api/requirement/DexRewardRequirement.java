package com.envyful.better.dex.rewards.forge.api.requirement;

import net.minecraft.server.level.ServerPlayer;

/**
 *
 * Something a player must meet, on top of the required dex, to claim a reward. Another mod adds a type
 * with {@link DexRewardRequirementRegistry#register(String, DexRewardRequirement)}, and a reward uses it
 * by that name in its config, with `data` passed through as written.
 *
 */
@FunctionalInterface
public interface DexRewardRequirement {

    boolean meets(ServerPlayer player, String data);

}
