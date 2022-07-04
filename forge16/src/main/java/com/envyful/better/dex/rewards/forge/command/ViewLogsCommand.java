package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.time.UtilTimeFormat;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Command(
        value = "viewlogs",
        description = "Views the logs",
        aliases = {
                "vl"
        }
)
@Permissible("better.dex.rewards.command.view.logs")
@Child
public class ViewLogsCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new StringTextComponent("Invalid args. /dexr viewlogs <uuid>"), Util.NIL_UUID);
            return;
        }

        Optional<UUID> uuid = this.parseUUID(args[0]);

        if (!uuid.isPresent()) {
            sender.sendMessage(new StringTextComponent("Invalid args. /dexr viewlogs <uuid>"), Util.NIL_UUID);
            return;
        }

        sender.sendMessage(new StringTextComponent("Find below the logs for " + uuid.get().toString()), Util.NIL_UUID);

        try (Connection connection = BetterDexRewards.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BetterDexRewardsQueries.LOAD_LOGS_USER)) {
            preparedStatement.setString(1, uuid.get().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                sender.sendMessage(new StringTextComponent(
                        "Rank: " + resultSet.getString("claimed_rank") + ", Time: " +
                                UtilTimeFormat.format(new Date(resultSet.getTimestamp("time_stamp").getTime()), "dd/MM/yyyy hh:mm:ss")
                        + ", commands: " + resultSet.getBlob("commands")
                ), Util.NIL_UUID);
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
