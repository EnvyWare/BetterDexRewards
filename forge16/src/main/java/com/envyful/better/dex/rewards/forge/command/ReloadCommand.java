package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import net.minecraft.command.ICommandSource;

import java.util.List;

@Command(
        value = {
                "reload",
                "r"
        }
)
@Permissible("better.dex.rewards.command.reload")
public class ReloadCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender, String[] args) {
        BetterDexRewards.getInstance().reloadConfig();
        PlatformProxy.sendMessage(sender, List.of("Reloaded config"));
    }
}
