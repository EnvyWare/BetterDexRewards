package com.envyful.better.dex.rewards.forge.config;

public class BetterDexRewardsQueries {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `better_dex_rewards_player_claims` (" +
            "id             INT             UNSIGNED        NOT NULL        AUTO_INCREMENT, " +
            "uuid           VARCHAR(64)     NOT NULL, " +
            "claimed_rank   VARCHAR(200)    NOT NULL, " +
            "UNIQUE(uuid, claimed_rank), " +
            "PRIMARY KEY(id));";

    public static final String CREATE_LOG_TABLE = "CREATE TABLE IF NOT EXISTS `better_dex_rewards_player_logs`(" +
            "id             INT             UNSIGNED        NOT NULL        AUTO_INCREMENT," +
            "uuid           VARCHAR(64)     NOT NULL, " +
            "claimed_rank   VARCHAR(200)    NOT NULL, " +
            "time_stamp     TIMESTAMP       NOT NULL        DEFAULT CURRENT_TIMESTAMP, " +
            "commands       BLOB            NOT NULL, " +
            "PRIMARY KEY(uuid));";

    public static final String LOAD_USER_CLAIMED = "SELECT claimed_rank FROM `better_dex_rewards_player_claims` WHERE uuid = ?;";

    public static final String ADD_USER_CLAIMED = "INSERT IGNORE INTO `better_dex_rewards_player_claims`(uuid, claimed_rank) " +
            "VALUES (?, ?);";

    public static final String LOAD_LOGS_USER = "SELECT * FROM `better_dex_rewards_player_logs` WHERE uuid = ?;";
    public static final String ADD_USER_LOGS = "INSERT INTO `better_dex_rewards_player_logs`(uuid, claimed_rank, commands) " +
            "VALUES (?, ?, ?);";

}
