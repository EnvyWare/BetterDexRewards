package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.config.UtilConfigInterface;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.gui.Transformer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexFormattedTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonNameTransformer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.transformer.SpeciesSpriteTransformer;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DexRewardsMissingUI {

    public static void open(EnvyPlayer<ServerPlayerEntity> player) {
        open(player, 1, false);
    }

    public static void open(EnvyPlayer<ServerPlayerEntity> player, int startPos, boolean backwards) {
        UtilConcurrency.runAsync(() -> {
            BetterDexRewardsGraphics.MissingPokemonUI config = BetterDexRewards.getInstance().getGraphics().getMissingPokemonUI();

            Pane pane = GuiFactory.paneBuilder()
                    .topLeftX(0)
                    .topLeftY(0)
                    .width(9)
                    .height(config.getGuiSettings().getHeight())
                    .build();

            UtilConfigInterface.fillBackground(pane, config.getGuiSettings());

            UtilConfigItem.addConfigItem(pane, config.getBackButton(), (envyPlayer, clickType) ->
                    UtilForgeConcurrency.runSync(() -> DexRewardsMainUI.open(player)));

            PlayerPartyStorage storage = StorageProxy.getParty(player.getParent());

            int i = 0;
            int speciesPosition = startPos;
            boolean backReset = false;
            boolean forwardReset = false;
            int dexSize = PixelmonSpecies.getAll().size();

            while (i < config.getMissingPokemonPositions().size()) {
                Species species = PixelmonSpecies.fromDex(speciesPosition).orElse(null);

                if (species == null || species.is(PixelmonSpecies.MISSINGNO) || storage.playerPokedex.hasSeen(species)) {
                    if (speciesPosition > dexSize) {
                        forwardReset = true;
                        break;
                    }

                    if (speciesPosition <= 0) {
                        backReset = true;
                        break;
                    }

                    if (backwards) {
                        --speciesPosition;
                    } else {
                        ++speciesPosition;
                    }

                    continue;
                }

                int position = config.getMissingPokemonPositions().get(backwards ? config.getMissingPokemonPositions().size() - i - 1 : i);
                pane.set(position % 9, position / 9, GuiFactory.displayable((UtilConfigItem.fromConfigItem(
                        config.getMissingPokemonItem(),
                        Lists.newArrayList(
                                PokemonDexTransformer.of(species),
                                PokemonNameTransformer.of(species),
                                PokemonDexFormattedTransformer.of(species),
                                SpeciesSpriteTransformer.of(species)
                        )
                ))));
                ++i;

                if (backwards) {
                    --speciesPosition;
                } else {
                    ++speciesPosition;
                }
            }

            boolean finalBackReset = backReset;
            int finalSpeciesPosition = speciesPosition;
            boolean finalForwardReset = forwardReset;
            UtilConfigItem.addConfigItem(pane, config.getNextPageButton(), (envyPlayer, clickType) ->
                    open(player, finalForwardReset || startPos == dexSize || finalSpeciesPosition == dexSize ? 1 : backwards ? startPos : finalSpeciesPosition, false));
            UtilConfigItem.addConfigItem(pane, config.getPreviousPageButton(), (envyPlayer, clickType) ->
                    open(player, finalBackReset || startPos == 1 ? dexSize : backwards ? finalSpeciesPosition : startPos - 1, true));

            GuiFactory.guiBuilder()
                    .addPane(pane)
                    .setCloseConsumer(envyPlayer -> {})
                    .setPlayerManager(BetterDexRewards.getInstance().getPlayerManager())
                    .height(config.getGuiSettings().getHeight())
                    .title(UtilChatColour.colour(config.getGuiSettings().getTitle()))
                    .build()
                    .open(player);
        });
    }
}
