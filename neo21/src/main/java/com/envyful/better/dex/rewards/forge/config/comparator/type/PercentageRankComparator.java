package com.envyful.better.dex.rewards.forge.config.comparator.type;

import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.better.dex.rewards.forge.config.comparator.RankComparator;
import com.pixelmonmod.pixelmon.api.pokedex.status.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
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
    public double getRequired() {
        return this.percentage;
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
        return player.getParent().getPokedexNow().getPercentage(PokedexRegistrationStatus.CAUGHT) >= this.required(reduction);
    }

    @Override
    public int distance(ForgeEnvyPlayer player, double reduction) {
        var caught = player.getParent().getPokedexNow().countCaught();
        var percentage = (this.required(reduction) / 100.0F) * PixelmonSpecies.getAll().size();
        return (int) Math.max(0, percentage - caught);
    }

    private double required(double reduction) {
        return Math.max(0, this.percentage - reduction);
    }
}
