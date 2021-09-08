package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.json.UtilGson;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Command(
        value = "convert",
        description = "Converts from other mods to this"
)
@Permissible("better.dex.rewards.command.convert")
@Child
public class ConvertCommand {

    @CommandProcessor
    public void runCommand(@Sender ICommandSender sender, String[] args) {
        UtilForgeConcurrency.runSync(() -> {
            File file = Paths.get("config/SimpleDexRewards/data.json").toFile();

            if (!file.exists()) {
                sender.sendMessage(new TextComponentString("There is no data to convert."));
                return;
            }

            try {
                JsonReader reader = new JsonReader(new FileReader(file));
                JsonObject list = UtilGson.GSON.fromJson(reader, JsonObject.class);

                try (Connection connection = BetterDexRewards.getInstance().getDatabase().getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(BetterDexRewardsQueries.ADD_USER_CLAIMED)) {
                    for (JsonElement map : list.get("playerData").getAsJsonArray()) {
                        JsonObject object = map.getAsJsonObject();
                        String uuid = object.get("uuid").getAsString();
                        JsonArray claimedRewards = object.get("claimedRewards").getAsJsonArray();

                        for (JsonElement claimedReward : claimedRewards) {
                            preparedStatement.setString(1, uuid);
                            preparedStatement.setString(2, claimedReward.getAsString());
                            preparedStatement.addBatch();
                        }
                    }

                    sender.sendMessage(new TextComponentString("Added all the users"));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });


    }
}
