package com.envyful.better.dex.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Completable;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.neoforge.command.completion.player.PlayerTabCompleter;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.platform.Messageable;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.dex.rewards.forge.player.DexRewardsAttribute;

import java.util.List;

@Command(
        value = {
                "resetplayer"
        }
)
@Permissible("betterdexrewards.command.resetplayer")
public class ResetPlayerCommand {

    @CommandProcessor
    public void onCommand(@Sender Messageable<?> player,
                          @Completable (PlayerTabCompleter.class) @Argument ForgeEnvyPlayer target,
                          String[] args) {
        var attribute = target.getAttributeNow(DexRewardsAttribute.class);
        attribute.clearClaims();
        PlatformProxy.sendMessage(player, List.of("Successfully reset " + target.getName() + "'s claims"));
    }
}
