package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = {
                "betterdexrewards",
                "dexrewards",
                "drewards",
                "dexr",
                "dex",
                "pokedex"
        }
)
@SubCommands({ReloadCommand.class, ResetPlayerCommand.class})
public class BetterDexRewardsCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayer player, String[] args) {
        BetterDexRewards.getGraphics().getUiSettings().open(BetterDexRewards.getPlayerManager().getPlayer(player), 1);
    }
}
