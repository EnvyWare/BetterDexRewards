package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.google.common.collect.Sets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class DexRewardsAttribute extends AbstractForgeAttribute<BetterDexRewards> {

    private Set<String> claimedRewards = Sets.newHashSet();

    public DexRewardsAttribute(BetterDexRewards manager, EnvyPlayer<?> parent) {
        super(manager, (ForgeEnvyPlayer) parent);
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

    @Override
    public void load() {

    }

    @Override
    public void save() {}
}
