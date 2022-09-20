package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;

public class BiomesTransformer implements Transformer {

    private final Stats pokemon;

    public static BiomesTransformer of(Stats species) {
        return new BiomesTransformer(species);
    }

    public BiomesTransformer(Stats pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String transformName(String name) {
        return name.replace("%biomes%", String.join("§7,§f ", UtilPokemonInfo.getSpawnBiomes(this.pokemon)));
    }
}
