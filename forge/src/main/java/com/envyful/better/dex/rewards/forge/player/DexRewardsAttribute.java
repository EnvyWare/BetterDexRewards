package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.storage.UtilPixelmonPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class DexRewardsAttribute extends AbstractForgeAttribute<BetterDexRewards> {

    private Set<String> claimedRewards = Sets.newHashSet();
    private long lastReminder = System.currentTimeMillis();

    public DexRewardsAttribute(BetterDexRewards manager, EnvyPlayer<?> parent) {
        super(manager, (ForgeEnvyPlayer) parent);
    }

    public long getLastReminder() {
        return this.lastReminder;
    }

    public void setLastReminder(long lastReminder) {
        this.lastReminder = lastReminder;
    }

    public void claimReward(String id) {
        this.claimedRewards.add(id);

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

    public boolean hasClaimed(String id) {
        return this.claimedRewards.contains(id);
    }

    public double getPokeDexPercentage() {
        PlayerPartyStorage storage = UtilPixelmonPlayer.getParty(this.parent.getParent());

        return (storage.pokedex.countCaught() / (double) Pokedex.pokedexSize) * 100.0;
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
