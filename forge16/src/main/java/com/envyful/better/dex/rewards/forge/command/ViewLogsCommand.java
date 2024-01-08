package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.time.UtilTimeFormat;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import net.minecraft.command.ICommandSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Command(
        value = "viewlogs"
)
@Permissible("better.dex.rewards.command.view.logs")
public class ViewLogsCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender, String[] args) {
        if (args.length != 1) {
            PlatformProxy.sendMessage(sender, List.of("Invalid args. /dexr viewlogs <uuid>"));
            return;
        }

        Optional<UUID> uuid = this.parseUUID(args[0]);

        if (!uuid.isPresent()) {
            PlatformProxy.sendMessage(sender, List.of("Invalid args. /dexr viewlogs <uuid>"));
            return;
        }

        PlatformProxy.sendMessage(sender, List.of("Find below the logs for " + uuid.get()));

        try (Connection connection = BetterDexRewards.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BetterDexRewardsQueries.LOAD_LOGS_USER)) {
            preparedStatement.setString(1, uuid.get().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PlatformProxy.sendMessage(sender, List.of("Rank: " + resultSet.getString("claimed_rank") + ", Time: " +
                        UtilTimeFormat.format(new Date(resultSet.getTimestamp("time_stamp").getTime()), "dd/MM/yyyy hh:mm:ss")
                        + ", commands: " + resultSet.getString("commands")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Optional<UUID> parseUUID(String s) {
        try {
            return Optional.of(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
