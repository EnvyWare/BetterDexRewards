package com.envyful.better.dex.rewards.forge.config.comparator.type;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparator;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CountRankComparator implements RankComparator {

    protected double percentage;

    public CountRankComparator() {
    }

    public CountRankComparator(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String id() {
        return "count";
    }

    @Override
    public boolean test(ForgeEnvyPlayer player) {
        return StorageProxy.getParty(player.getParent()).playerPokedex.countCaught() >= this.percentage;
    }
}
