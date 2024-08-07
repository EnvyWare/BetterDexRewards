package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.database.sql.SqlType;
import com.envyful.api.database.sql.UtilSql;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.forge.player.attribute.ManagedForgeAttribute;
import com.envyful.api.player.SaveMode;
import com.envyful.api.player.save.attribute.DataDirectory;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.envyful.better.dex.rewards.forge.config.DexCompletion;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.species.Pokedex;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;

import java.util.Set;

@DataDirectory("config/players/BetterDexRewards/")
public class DexRewardsAttribute extends ManagedForgeAttribute<BetterDexRewards> {

    private Set<String> claimedRewards = Sets.newHashSet();
    private long lastReminder = System.currentTimeMillis();

    public DexRewardsAttribute() {
        super(BetterDexRewards.getInstance());
    }

    public long getLastReminder() {
        return this.lastReminder;
    }

    public void setLastReminder(long lastReminder) {
        this.lastReminder = lastReminder;
    }

    public void claimReward(String id) {
        this.claimedRewards.add(id);

        if (BetterDexRewards.getConfig().getSaveMode() == SaveMode.MYSQL) {
            UtilSql.update(this.manager.getDatabase())
                    .query(BetterDexRewardsQueries.ADD_USER_CLAIMED)
                    .data(SqlType.text(this.id.toString()), SqlType.text(id))
                    .executeAsync();
        }
    }

    public void clearClaims() {
        UtilSql.update(BetterDexRewards.getInstance().getDatabase())
                .query(BetterDexRewardsQueries.CLEAR_USER)
                .data(SqlType.text(this.id.toString()))
                .executeAsync();

        this.claimedRewards.clear();
    }

    public boolean hasClaimed(DexCompletion stage) {
        return this.claimedRewards.contains(stage.getId());
    }

    public double getPokeDexPercentage() {
        PlayerPartyStorage storage = StorageProxy.getPartyNow(this.parent.getParent());

        return (storage.playerPokedex.countCaught() / (double) Pokedex.pokedexSize) * 100.0;
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

    @Override
    public void load() {
        UtilSql.query(this.manager.getDatabase())
                .query(BetterDexRewardsQueries.LOAD_USER_CLAIMED)
                .data(SqlType.text(this.id.toString()))
                .converter(resultSet -> {
                    this.claimedRewards.add(resultSet.getString("claimed_rank"));
                    return null;
                })
                .executeAsyncWithConverter();
    }

    @Override
    public void save() {}
}
