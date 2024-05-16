package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Completable;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.database.sql.SqlType;
import com.envyful.api.database.sql.UtilSql;
import com.envyful.api.forge.command.completion.player.PlayerTabCompleter;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsQueries;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;
import com.envyful.better.dex.rewards.forge.ui.DexRewardsMainUI;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

@Command(
        value = {
                "resetplayer"
        }
)
@Permissible("betterdexrewards.command.resetplayer")
public class ResetPlayerCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource player,
                          @Completable (PlayerTabCompleter.class) @Argument ForgeEnvyPlayer target,
                          String[] args) {
        var attribute = target.getAttributeNow(DexRewardsAttribute.class);
        attribute.clearClaims();
        PlatformProxy.sendMessage(player, List.of("Successfully reset " + target.getName() + "'s claims"));
    }
}
