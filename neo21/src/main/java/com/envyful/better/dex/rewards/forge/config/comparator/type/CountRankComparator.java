package com.envyful.better.dex.rewards.forge.config.comparator.type;

import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparator;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CountRankComparator implements RankComparator {

    protected double count;

    public CountRankComparator() {
    }

    public CountRankComparator(double count) {
        this.count = count;
    }

    @Override
    public String id() {
        return "count";
    }

    @Override
    public boolean test(ForgeEnvyPlayer player) {
        return player.getParent().getPartyNow().playerPokedex.countCaught() >= this.count;
    }

    @Override
    public int distance(ForgeEnvyPlayer player) {
        var caught =  player.getParent().getPartyNow().playerPokedex.countCaught();
        return (int) Math.max(0, this.count - caught);
    }
}
