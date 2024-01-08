package com.envyful.better.dex.rewards.forge.config.comparator.type;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparator;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class PercentageRankComparator implements RankComparator {

    protected double percentage;

    public PercentageRankComparator() {
    }

    public PercentageRankComparator(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String id() {
        return "percentage";
    }

    @Override
    public boolean test(ForgeEnvyPlayer player) {
        return StorageProxy.getPartyNow(player.getParent()).playerPokedex.getCaughtCompletionPercentage() >= this.percentage;
    }
}
