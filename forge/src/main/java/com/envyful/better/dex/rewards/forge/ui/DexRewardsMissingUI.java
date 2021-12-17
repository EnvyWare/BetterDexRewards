package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.storage.UtilPixelmonPlayer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexFormattedTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonNameTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonSpriteTransformer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.transformer.BiomesTransformer;
import com.envyful.better.dex.rewards.forge.transformer.CatchRateTransformer;
import com.envyful.better.dex.rewards.forge.transformer.SpawnTimesTransformer;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class DexRewardsMissingUI {

    public static void open(EnvyPlayer<EntityPlayerMP> player) {
        open(player, 0, false);
    }

    public static void open(EnvyPlayer<EntityPlayerMP> player, int startPos, boolean backwards) {
        ConfigInterface config = BetterDexRewards.getInstance().getConfig().getConfigInterface();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftX(0)
                .topLeftY(0)
                .width(9)
                .height(6)
                .build();


        for (ConfigItem fillerItem : config.getFillerItems()) {
            pane.add(GuiFactory.displayable(UtilConfigItem.fromConfigItem(fillerItem)));
        }

        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getBackButton(),
                                     (envyPlayer, clickType) -> {
                                         ((EntityPlayerMP) envyPlayer.getParent()).closeScreen();

                                         UtilForgeConcurrency.runSync(() -> {
                                             DexRewardsMainUI.open(player);
                                         });
                                     }
        );

        List<EnumSpecies> values = Lists.newArrayList(EnumSpecies.values());
        PlayerPartyStorage storage = UtilPixelmonPlayer.getParty(player.getParent());
        int counter = 0;
        int pos;
        int lastDex;

        if (backwards) {
            counter = 35;
            for (pos = startPos; counter >= 0 && pos >= 0; pos--) {
                EnumSpecies species = values.get(pos);

                if (species == EnumSpecies.MissingNo || storage.pokedex.hasCaught(species)) {
                    continue;
                }

                pane.set(counter % 9, counter / 9, GuiFactory.displayable((UtilConfigItem.fromConfigItem(
                        BetterDexRewards.getInstance().getConfig().getMissingPokemonItem(),
                        Lists.newArrayList(
                                PokemonDexTransformer.of(species),
                                PokemonNameTransformer.of(species),
                                PokemonDexFormattedTransformer.of(species),
                                PokemonSpriteTransformer.of(species),
                                BiomesTransformer.of(species),
                                CatchRateTransformer.of(species),
                                SpawnTimesTransformer.of(species)
                        )
                ))));

                --counter;
            }
        } else {
            for (pos = startPos; counter < 36 && values.size() > pos; pos++) {
                EnumSpecies species = values.get(pos);

                if (species == EnumSpecies.MissingNo || storage.pokedex.hasCaught(species)) {
                    continue;
                }

                pane.set(counter % 9, counter / 9, GuiFactory.displayable((UtilConfigItem.fromConfigItem(
                        BetterDexRewards.getInstance().getConfig().getMissingPokemonItem(),
                        Lists.newArrayList(
                                PokemonDexTransformer.of(species),
                                PokemonNameTransformer.of(species),
                                PokemonDexFormattedTransformer.of(species),
                                PokemonSpriteTransformer.of(species),
                                BiomesTransformer.of(species),
                                CatchRateTransformer.of(species),
                                SpawnTimesTransformer.of(species)
                        )
                ))));

                ++counter;
            }
        }

        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getPreviousPageButton(),
                                     (envyPlayer, clickType) -> open((EnvyPlayer<EntityPlayerMP>) envyPlayer,
                                                                     startPos == 0 ? values.size() - 1 : startPos - 1,
                                                                     true)
        );

        int finalPos = pos;
        int finalCounter = counter;
        UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getNextPageButton(),
                                     (envyPlayer, clickType) -> open((EnvyPlayer<EntityPlayerMP>) envyPlayer,
                                                                     finalCounter < 36 || values.size() <= finalPos ? 0 : finalPos, false)
        );

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getHeight())
                .title(UtilChatColour.translateColourCodes('&', config.getTitle()))
                .build().open(player);
    }
}
