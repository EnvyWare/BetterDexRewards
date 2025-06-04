package com.envyful.better.dex.rewards.forge.player;

import com.envyful.api.database.sql.SqlType;
import com.envyful.api.database.sql.UtilSql;
import com.envyful.api.player.attribute.adapter.AttributeAdapter;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;

import java.util.concurrent.CompletableFuture;

public class SQLiteAttributeAdapter implements AttributeAdapter<DexRewardsAttribute>, DexRewardsAdapter {

    public static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS `better_dex_rewards_player_claims` (
            id             INT                     NOT NULL        ,
            uuid           TEXT     NOT NULL,
            claimed_rank   TEXT    NOT NULL,
            UNIQUE(uuid, claimed_rank),
            PRIMARY KEY(id));
            """;

    public static final String LOAD_USER_CLAIMED = "SELECT claimed_rank FROM `better_dex_rewards_player_claims` WHERE uuid = ?;";

    public static final String ADD_USER_CLAIMED = "INSERT IGNORE INTO `better_dex_rewards_player_claims`(uuid, claimed_rank) VALUES (?, ?);";

    public static final String CLEAR_USER = "DELETE * FROM `better_dex_rewards_player_claims` WHERE uuid = ?;";

    @Override
    public CompletableFuture<Void> save(DexRewardsAttribute dexRewardsAttribute) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void load(DexRewardsAttribute dexRewardsAttribute) {
        UtilSql.query(BetterDexRewards.getInstance().getDatabase())
                .query(LOAD_USER_CLAIMED)
                .data(SqlType.text(dexRewardsAttribute.getUniqueId().toString()))
                .converter(resultSet -> {
                    dexRewardsAttribute.claimedRewards.add(resultSet.getString("claimed_rank"));
                    return null;
                })
                .executeAsyncWithConverter();
    }

    @Override
    public CompletableFuture<Void> delete(DexRewardsAttribute dexRewardsAttribute) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return null;
    }

    @Override
    public void initialize() {
        UtilSql.update(BetterDexRewards.getInstance().getDatabase())
                .query(CREATE_TABLE)
                .executeAsync();
    }

    @Override
    public void claimReward(DexRewardsAttribute attribute, String id) {
        UtilSql.update(BetterDexRewards.getInstance().getDatabase())
                .query(ADD_USER_CLAIMED)
                .data(SqlType.text(attribute.getUniqueId().toString()), SqlType.text(id))
                .executeAsync();
    }

    @Override
    public void clearClaims(DexRewardsAttribute attribute) {
        UtilSql.update(BetterDexRewards.getInstance().getDatabase())
                .query(CLEAR_USER)
                .data(SqlType.text(attribute.getUniqueId().toString()))
                .executeAsync();
    }
}
