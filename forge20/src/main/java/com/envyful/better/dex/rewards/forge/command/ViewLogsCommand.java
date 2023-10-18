package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.time.UtilTimeFormat;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Command(
        value = {
                "viewlogs",
                "vl"
        }
)
@Permissible("better.dex.rewards.command.view.logs")
public class ViewLogsCommand {

    @CommandProcessor
    public void onCommand(@Sender CommandSource sender, String[] args) {
        if (args.length != 1) {
            sender.sendSystemMessage(Component.literal("Invalid args. /dexr viewlogs <uuid>"));
            return;
        }

        Optional<UUID> uuid = this.parseUUID(args[0]);

        if (!uuid.isPresent()) {
            sender.sendSystemMessage(Component.literal("Invalid args. /dexr viewlogs <uuid>"));
            return;
        }

        sender.sendSystemMessage(Component.literal("Find below the logs for " + uuid.get()));

        try (Connection connection = BetterDexRewards.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BetterDexRewardsQueries.LOAD_LOGS_USER)) {
            preparedStatement.setString(1, uuid.get().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                sender.sendSystemMessage(Component.literal(
                        "Rank: " + resultSet.getString("claimed_rank") + ", Time: " +
                                UtilTimeFormat.format(new Date(resultSet.getTimestamp("time_stamp").getTime()), "dd/MM/yyyy hh:mm:ss")
                        + ", commands: " + resultSet.getString("commands")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Optional<UUID> parseUUID(String s) {
        try {
            return Optional.ofNullable(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
