package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.forge.player.attribute.ManagedForgeAttribute;
import com.envyful.api.player.SaveMode;
import com.envyful.api.player.save.attribute.DataDirectory;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.species.Pokedex;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@DataDirectory("config/players/BetterDexRewards/")
public class DexRewardsAttribute extends ManagedForgeAttribute<BetterDexRewards> {

    private Set<String> claimedRewards = Sets.newHashSet();
    private long lastReminder = System.currentTimeMillis();

    public DexRewardsAttribute(ForgePlayerManager playerManager) {
        super(BetterDexRewards.getInstance(), playerManager);
    }

    public long getLastReminder() {
        return this.lastReminder;
    }

    public void setLastReminder(long lastReminder) {
        this.lastReminder = lastReminder;
    }

    public void claimReward(String id) {
        this.claimedRewards.add(id);

        if (this.manager.getConfig().getSaveMode() == SaveMode.MYSQL) {
            UtilConcurrency.runAsync(() -> {
                try (Connection connection = this.manager.getDatabase().getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(BetterDexRewardsQueries.ADD_USER_CLAIMED)) {
                    preparedStatement.setString(1, this.parent.getUuid().toString());
                    preparedStatement.setString(2, id);

                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public boolean hasClaimed(String id) {
        return this.claimedRewards.contains(id);
    }

    public double getPokeDexPercentage() {
        PlayerPartyStorage storage = StorageProxy.getPartyNow(this.parent.getParent());

        return (storage.playerPokedex.countCaught() / (double) Pokedex.pokedexSize) * 100.0;
    }

    @Override
    public void load() {
        try (Connection connection = this.manager.getDatabase().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(BetterDexRewardsQueries.LOAD_USER_CLAIMED)) {
            preparedStatement.setString(1, this.parent.getUuid().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                this.claimedRewards.add(resultSet.getString("claimed_rank"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {}
}
