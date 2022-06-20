package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.ui.DexRewardsMainUI;
import net.minecraft.entity.player.ServerPlayerEntity;

@Command(
        value = "betterdexrewards",
        description = "Root command",
        aliases = {
                "dexrewards",
                "drewards",
                "dexr",
                "dex",
                "pokedex"
        }
)
@SubCommands({ConvertCommand.class, ReloadCommand.class})
public class BetterDexRewardsCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayerEntity player, String[] args) {
        DexRewardsMainUI.open(BetterDexRewards.getInstance().getPlayerManager().getPlayer(player));
    }
}
