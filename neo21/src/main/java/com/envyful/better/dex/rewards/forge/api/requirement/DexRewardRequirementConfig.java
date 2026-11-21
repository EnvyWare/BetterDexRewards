package com.envyful.better.dex.rewards.forge.api.requirement;

import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.google.common.collect.Sets;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Set;

@ConfigSerializable
public class DexRewardRequirementConfig {

    private static final Set<String> WARNED_TYPES = Sets.newConcurrentHashSet();

    private String type;
    private String data;
    private List<String> failedMessage = List.of();

    public DexRewardRequirementConfig() {}

    public DexRewardRequirementConfig(String type, String data, List<String> failedMessage) {
        this.type = type;
        this.data = data;
        this.failedMessage = failedMessage;
    }

    public List<String> getFailedMessage() {
        return this.failedMessage;
    }

    /**
     *
     * The type is looked up on each check rather than when the config loads, so it does not matter
     * whether the mod that registers it loaded before this one. An unknown type is a config mistake or
     * a missing mod, and refuses the claim, because a reward paid by mistake can't be taken back. The
     * reminder task checks often, so it is only logged once per type.
     *
     */
    public boolean meets(ServerPlayer player) {
        return meets(player, this.type, this.data);
    }

    /**
     *
     * Shared with stage bonuses, which name a type and data the same way, so an unknown type is
     * treated the same there: the bonus counts as unmet.
     *
     */
    public static boolean meets(ServerPlayer player, String type, String data) {
        var requirement = DexRewardRequirementRegistry.get(String.valueOf(type));

        if (requirement.isEmpty()) {
            if (WARNED_TYPES.add(String.valueOf(type))) {
                BetterDexRewards.getLogger().warn("Dex reward requirement type `{}` is not registered, so rewards and bonuses using it can't be claimed", type);
            }

            return false;
        }

        return requirement.get().meets(player, data);
    }
}
