package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.google.common.collect.Sets;

import java.util.Set;

public class DexRewardsAttribute extends AbstractForgeAttribute<BetterDexRewards> {

    private Set<String> claimedRewards = Sets.newHashSet();

    public DexRewardsAttribute(BetterDexRewards manager, EnvyPlayer<?> parent) {
        super(manager, (ForgeEnvyPlayer) parent);
    }

    public void claimReward(String id) {
        this.claimedRewards.add(id);
    }

    public boolean hasClaimed(String id) {
        return this.claimedRewards.contains(id);
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
