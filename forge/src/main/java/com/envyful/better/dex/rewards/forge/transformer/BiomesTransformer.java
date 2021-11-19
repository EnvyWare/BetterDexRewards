package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class BiomesTransformer implements Transformer {

    private final EnumSpecies pokemon;

    public static BiomesTransformer of(EnumSpecies species) {
        return new BiomesTransformer(species);
    }

    public BiomesTransformer(EnumSpecies pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String transformName(String name) {
        return name.replace("%biomes%", String.join("§7,§f ", UtilPokemonInfo.getSpawnBiomes(this.pokemon.getBaseStats())));
    }
}
