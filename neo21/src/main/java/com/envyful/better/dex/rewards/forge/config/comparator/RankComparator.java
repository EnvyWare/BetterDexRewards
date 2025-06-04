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

    static RankComparator percentage(double percentage) {
        return new PercentageRankComparator(percentage);
    }

    static RankComparator count(double count) {
        return new CountRankComparator(count);
    }

}
