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
    public double getRequired() {
        return this.count;
    }

    @Override
    public boolean test(ForgeEnvyPlayer player) {
        return this.test(player, 0);
    }

    @Override
    public int distance(ForgeEnvyPlayer player) {
        return this.distance(player, 0);
    }

    @Override
    public boolean test(ForgeEnvyPlayer player, double reduction) {
        return player.getParent().getPokedexNow().countCaught() >= this.required(reduction);
    }

    @Override
    public int distance(ForgeEnvyPlayer player, double reduction) {
        var caught =  player.getParent().getPokedexNow().countCaught();
        return (int) Math.max(0, this.required(reduction) - caught);
    }

    private double required(double reduction) {
        return Math.max(0, this.count - reduction);
    }
}
