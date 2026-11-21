package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.neoforge.player.attribute.ManagedForgeAttribute;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.envyful.better.dex.rewards.forge.config.DexRewardBonus;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;

import java.util.List;
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
        ((DexRewardsAdapter) BetterDexRewards.getPlayerManager().getAdapter(DexRewardsAttribute.class)).claimReward(this, id);
    }

    public boolean hasClaimed(DexCompletion stage) {
        return this.claimedRewards.contains(stage.getId());
    }

    public boolean hasClaimed(DexCompletion stage, DexRewardBonus bonus) {
        return this.claimedRewards.contains(stage.getBonusClaimId(bonus));
    }

    public void claimBonus(DexCompletion stage, DexRewardBonus bonus) {
        this.claimReward(stage.getBonusClaimId(bonus));
    }

    /**
     *
     * The bonuses of the stage the player meets now and has not been paid for, whether or not the stage
     * itself is claimed yet.
     *
     */
    public List<DexRewardBonus> getUnclaimedMetBonuses(DexCompletion stage) {
        return stage.getMetBonuses(this.parent.getParent()).stream()
                .filter(bonus -> !this.hasClaimed(stage, bonus))
                .toList();
    }

    /**
     *
     * A bonus can only be paid on its own once its stage has been claimed: before that it is paid with
     * the stage.
     *
     */
    public boolean hasPendingBonus(DexCompletion stage) {
        return this.hasClaimed(stage) && !this.getUnclaimedMetBonuses(stage).isEmpty();
    }

    public boolean isBlockedFromClaiming(DexCompletion stage) {
        return stage.getOptionalAntiClaimPermission() != null &&
                PlatformProxy.hasPermission(this.parent, stage.getOptionalAntiClaimPermission());
    }

    /**
     *
     * The first stage with something to claim: either the stage itself, with the dex it needs after
     * bonuses and its requirements met, or a bonus met since the stage was claimed.
     *
     */
    public DexCompletion findClaimableStage() {
        for (var stage : this.manager.getConfig().getRewardStages()) {
            if (this.isBlockedFromClaiming(stage)) {
                continue;
            }

            if (this.hasClaimed(stage)) {
                if (this.hasPendingBonus(stage)) {
                    return stage;
                }

                continue;
            }

            if (stage.meetsRequiredDex(this.parent) && stage.meetsRequirements(this.parent.getParent())) {
                return stage;
            }
        }

        return null;
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

            var currentDistance = rewardStage.getDexDistance(this.parent);

            if (currentDistance < smallestDistance) {
                smallestDistance = currentDistance;
                closest = rewardStage;
            }
        }

        return closest;
    }
}
