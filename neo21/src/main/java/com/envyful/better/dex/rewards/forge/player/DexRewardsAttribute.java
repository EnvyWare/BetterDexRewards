package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.neoforge.player.attribute.ManagedForgeAttribute;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;

import java.util.Set;
import java.util.UUID;

public class DexRewardsAttribute extends ManagedForgeAttribute<BetterDexRewards> {

    protected Set<String> claimedRewards = Sets.newHashSet();
    protected long lastReminder = System.currentTimeMillis();

    public DexRewardsAttribute(UUID id) {
        super(id, BetterDexRewards.getInstance());
    }

    public long getLastReminder() {
        return this.lastReminder;
    }

    public void setLastReminder(long lastReminder) {
        this.lastReminder = lastReminder;
    }

    public void claimReward(String id) {
        this.claimedRewards.add(id);
        ((DexRewardsAdapter) BetterDexRewards.getInstance().getPlayerManager().getAdapter(DexRewardsAttribute.class)).claimReward(this, id);
    }

    public boolean hasClaimed(DexCompletion stage) {
        return this.claimedRewards.contains(stage.getId());
    }

    public void clearClaims() {
        this.claimedRewards.clear();
        ((DexRewardsAdapter) BetterDexRewards.getInstance().getPlayerManager().getAdapter(DexRewardsAttribute.class)).clearClaims(this);
    }

    public double getPokeDexPercentage() {
        var pokedex = this.parent.getParent().getPokedexNow();

        return (pokedex.countCaught() / (double) PixelmonSpecies.getAll().size()) * 100.0;
    }

    public DexCompletion findNextStage() {
        var smallestDistance = 10_000;
        DexCompletion closest = null;

        for (var rewardStage : this.manager.getConfig().getRewardStages()) {
            if (this.hasClaimed(rewardStage)) {
                continue;
            }

            var currentDistance = rewardStage.getRequiredDex().distance(this.parent);

            if (currentDistance < smallestDistance) {
                smallestDistance = currentDistance;
                closest = rewardStage;
            }
        }

        return closest;
    }
}
