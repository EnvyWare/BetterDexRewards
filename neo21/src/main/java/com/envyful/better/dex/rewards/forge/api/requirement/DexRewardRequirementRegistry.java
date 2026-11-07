package com.envyful.better.dex.rewards.forge.api.requirement;

import com.envyful.api.neoforge.player.util.UtilPlayer;
import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class DexRewardRequirementRegistry {

    private static final Map<String, DexRewardRequirement> REGISTERED = Maps.newConcurrentMap();

    private DexRewardRequirementRegistry() {}

    public static void init() {
        register("permission", UtilPlayer::hasPermission);
    }

    public static void register(String type, DexRewardRequirement requirement) {
        REGISTERED.put(type.toLowerCase(Locale.ROOT), requirement);
    }

    public static Optional<DexRewardRequirement> get(String type) {
        return Optional.ofNullable(REGISTERED.get(type.toLowerCase(Locale.ROOT)));
    }
}
