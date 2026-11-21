package com.envyful.better.dex.rewards.forge.config.comparator;

import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.better.dex.rewards.forge.config.comparator.type.CountRankComparator;
import com.envyful.better.dex.rewards.forge.config.comparator.type.PercentageRankComparator;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public interface RankComparator {

    String id();

    boolean test(ForgeEnvyPlayer player);

    int distance(ForgeEnvyPlayer player);

    /**
     *
     * The threshold as written in the config, in this comparator's own unit (a count of Pokemon, or
     * percentage points). Stage bonuses subtract from it, so it is shown to players as `%base_dex%`.
     *
     */
    default double getRequired() {
        return 0;
    }

    /**
     *
     * The test with the threshold lowered by `reduction`, never below zero. A comparator registered by
     * another mod before bonuses existed only implements {@link #test(ForgeEnvyPlayer)} and
     * {@link #distance(ForgeEnvyPlayer)}, so these fall back to them and ignore the reduction.
     *
     */
    default boolean test(ForgeEnvyPlayer player, double reduction) {
        return this.test(player);
    }

    default int distance(ForgeEnvyPlayer player, double reduction) {
        return this.distance(player);
    }

    static RankComparator percentage(double percentage) {
        return new PercentageRankComparator(percentage);
    }

    static RankComparator count(double count) {
        return new CountRankComparator(count);
    }

}
