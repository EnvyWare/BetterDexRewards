package com.envyful.better.dex.rewards.forge.config;

import com.envyful.api.neoforge.config.ConfigReward;
import com.envyful.api.neoforge.config.ConfigRewardPool;
import com.envyful.better.dex.rewards.forge.api.requirement.DexRewardRequirementConfig;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 *
 * An optional extra on a dex reward stage. Unlike a requirement it never blocks the claim: while the
 * player meets it, the stage needs `dex-reduction` less dex, and the first claim of the stage after it
 * is met (or a later click on the claimed stage) pays its rewards once. `type` and `data` are looked up
 * in {@link com.envyful.better.dex.rewards.forge.api.requirement.DexRewardRequirementRegistry} exactly
 * like a requirement's.
 *
 */
@ConfigSerializable
public class DexRewardBonus {

    private String id;
    private String type;
    private String data;
    private double dexReduction = 0;
    private ConfigRewardPool<ConfigReward> rewards = null;
    private String description = "";

    public DexRewardBonus() {}

    public DexRewardBonus(String id, String type, String data, double dexReduction,
                          ConfigRewardPool<ConfigReward> rewards, String description) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.dexReduction = dexReduction;
        this.rewards = rewards;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }

    /**
     *
     * Clamped at zero, so a negative value in the config can't raise the stage's threshold.
     *
     */
    public double getDexReduction() {
        return Math.max(0, this.dexReduction);
    }

    public ConfigRewardPool<ConfigReward> getRewards() {
        return this.rewards;
    }

    public String getDescription() {
        return this.description == null ? "" : this.description;
    }

    public boolean meets(ServerPlayer player) {
        return DexRewardRequirementConfig.meets(player, this.type, this.data);
    }

    public void give(ServerPlayer player) {
        if (this.rewards != null) {
            this.rewards.give(player);
        }
    }
}
