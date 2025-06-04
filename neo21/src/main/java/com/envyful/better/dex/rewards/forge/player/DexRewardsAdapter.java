package com.envyful.better.dex.rewards.forge.player;

public interface DexRewardsAdapter {

    void claimReward(DexRewardsAttribute attribute, String id);

    void clearClaims(DexRewardsAttribute attribute);

}
