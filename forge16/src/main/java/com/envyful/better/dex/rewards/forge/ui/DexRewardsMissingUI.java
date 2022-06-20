package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexFormattedTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonNameTransformer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.transformer.SpeciesSpriteTransformer;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class DexRewardsMissingUI {

    public static void open(EnvyPlayer<ServerPlayerEntity> player) {
        open(player, 0, false);
    }

    public static void open(EnvyPlayer<ServerPlayerEntity> player, int startPos, boolean backwards) {
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
                                     (envyPlayer, clickType) -> DexRewardsMainUI.open(player));

        List<Species> values = Lists.newArrayList(PixelmonSpecies.getAll());
        PlayerPartyStorage storage = StorageProxy.getParty(player.getParent());
        int counter = 0;
        int pos;

        if (backwards) {
            counter = 35;
            for (pos = startPos; counter >= 0 && pos >= 0; pos--) {
                Species species = values.get(pos);

                if (species.is(PixelmonSpecies.MISSINGNO) || storage.playerPokedex.hasCaught(species)) {
                    continue;
                }

                pane.set(counter % 9, counter / 9, GuiFactory.displayable((UtilConfigItem.fromConfigItem(
                        BetterDexRewards.getInstance().getConfig().getMissingPokemonItem(),
                        Lists.newArrayList(
                                PokemonDexTransformer.of(species),
                                PokemonNameTransformer.of(species),
                                PokemonDexFormattedTransformer.of(species),
                                SpeciesSpriteTransformer.of(species)
                        )
                ))));

                --counter;
            }

            int finalCounter = counter;
            int finalPos = pos;
            UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getPreviousPageButton(),
                                         (envyPlayer, clickType) -> open((EnvyPlayer<ServerPlayerEntity>) envyPlayer,
                                                                         finalPos == 0 ? values.size() - 1 : finalPos, true)
            );

            UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getNextPageButton(),
                                         (envyPlayer, clickType) -> open((EnvyPlayer<ServerPlayerEntity>) envyPlayer,
                                                                         finalCounter > -1 || values.size() <= finalPos ? 0 : startPos + 1, false)
            );
        } else {
            for (pos = startPos; counter < 36 && values.size() > pos; pos++) {
                Species species = values.get(pos);

                if (species.is(PixelmonSpecies.MISSINGNO) || storage.playerPokedex.hasCaught(species)) {
                    continue;
                }

                pane.set(counter % 9, counter / 9, GuiFactory.displayable((UtilConfigItem.fromConfigItem(
                        BetterDexRewards.getInstance().getConfig().getMissingPokemonItem(),
                        Lists.newArrayList(
                                PokemonDexTransformer.of(species),
                                PokemonNameTransformer.of(species),
                                PokemonDexFormattedTransformer.of(species),
                                SpeciesSpriteTransformer.of(species)
                        )
                ))));

                ++counter;
            }

            UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getPreviousPageButton(),
                                         (envyPlayer, clickType) -> open((EnvyPlayer<ServerPlayerEntity>) envyPlayer,
                                                                         startPos == 0 ? values.size() - 1 : startPos - 1,
                                                                         true)
            );

            int finalPos = pos;
            int finalCounter = counter;
            UtilConfigItem.addConfigItem(pane, BetterDexRewards.getInstance().getConfig().getNextPageButton(),
                                         (envyPlayer, clickType) -> open((EnvyPlayer<ServerPlayerEntity>) envyPlayer,
                                                                         finalCounter < 36 || values.size() <= finalPos ? 0 : finalPos, false)
            );
        }

        GuiFactory.guiBuilder()
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                .height(config.getHeight())
                .title(UtilChatColour.translateColourCodes('&', config.getTitle()))
                .build().open(player);
    }
}
