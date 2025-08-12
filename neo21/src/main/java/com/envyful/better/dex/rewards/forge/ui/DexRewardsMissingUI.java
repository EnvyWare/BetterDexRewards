package com.envyful.better.dex.rewards.forge.ui;

import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.neoforge.config.UtilConfigItem;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexFormattedTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonDexTransformer;
import com.envyful.api.reforged.pixelmon.transformer.PokemonNameTransformer;
import com.envyful.better.dex.rewards.forge.BetterDexRewards;
import com.envyful.better.dex.rewards.forge.config.BetterDexRewardsGraphics;
import com.envyful.better.dex.rewards.forge.transformer.BiomesTransformer;
import com.envyful.better.dex.rewards.forge.transformer.CatchRateTransformer;
import com.envyful.better.dex.rewards.forge.transformer.PokemonSpriteTransformer;
import com.envyful.better.dex.rewards.forge.transformer.SpawnTimesTransformer;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;

public class DexRewardsMissingUI {

    public static void open(ForgeEnvyPlayer player) {
        open(player, 1, false);
    }

    public static void open(ForgeEnvyPlayer player, int startPos, boolean backwards) {
//            BetterDexRewardsGraphics.MissingPokemonUI config = BetterDexRewards.getInstance().getGraphics().getMissingPokemonUI();
//            var pane = config.getGuiSettings().toPane();
//
//            UtilConfigItem.builder()
//                            .asyncClick()
//                            .clickHandler((envyPlayer, clickType) -> DexRewardsMainUI.open(player))
//                            .extendedConfigItem(player, pane, config.getBackButton());
//
//            var storage = player.getParent().getPokedexNow();
//            int i = 0;
//            int speciesPosition = startPos;
//            boolean backReset = false;
//            boolean forwardReset = false;
//            int dexSize = PixelmonSpecies.getAll().size();
//
//            while (i < config.getMissingPokemonPositions().size()) {
//                Species species = PixelmonSpecies.fromDex(speciesPosition).orElse(null);
//
//                if (species == null || species.is(PixelmonSpecies.MISSINGNO) || storage.hasCaught(species)) {
//                    if (speciesPosition > dexSize) {
//                        forwardReset = true;
//                        break;
//                    }
//
//                    if (speciesPosition <= 0) {
//                        backReset = true;
//                        break;
//                    }
//
//                    if (backwards) {
//                        --speciesPosition;
//                    } else {
//                        ++speciesPosition;
//                    }
//
//                    continue;
//                }
//
//                int position = config.getMissingPokemonPositions().get(backwards ? config.getMissingPokemonPositions().size() - i - 1 : i);
//                pane.set(position % 9, position / 9, GuiFactory.displayable((UtilConfigItem.fromConfigItem(
//                        config.getMissingPokemonItem(),
//                        Lists.newArrayList(
//                                PokemonDexTransformer.of(species),
//                                PokemonNameTransformer.of(species),
//                                PokemonDexFormattedTransformer.of(species),
//                                new PokemonSpriteTransformer(species.getDefaultForm().getDefaultGenderProperties().getDefaultPalette().getSprite().toString()),
//                                BiomesTransformer.of(species.getDefaultForm()),
//                                CatchRateTransformer.of(species.getDefaultForm()),
//                                SpawnTimesTransformer.of(species.getDefaultForm())
//                        )
//                ))));
//                ++i;
//
//                if (backwards) {
//                    --speciesPosition;
//                } else {
//                    ++speciesPosition;
//                }
//            }
//
//            boolean finalBackReset = backReset;
//            int finalSpeciesPosition = speciesPosition;
//            boolean finalForwardReset = forwardReset;
//
//        UtilConfigItem.builder()
//                .asyncClick()
//                .clickHandler((envyPlayer, clickType) ->
//                        open(player, finalForwardReset || startPos == dexSize || finalSpeciesPosition == dexSize ? 1 : backwards ? startPos : finalSpeciesPosition, false))
//                .extendedConfigItem(player, pane, config.getNextPageButton());
//
//        UtilConfigItem.builder()
//                .asyncClick()
//                .clickHandler((envyPlayer, clickType) ->
//                        open(player, finalBackReset || startPos == 1 ? dexSize : backwards ? finalSpeciesPosition : startPos - 1, true))
//                .extendedConfigItem(player, pane, config.getPreviousPageButton());
//
//        pane.open(player, config.getGuiSettings());
    }
}
