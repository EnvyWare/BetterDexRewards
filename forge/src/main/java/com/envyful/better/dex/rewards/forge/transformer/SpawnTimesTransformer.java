package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;
import com.envyful.api.reforged.pixelmon.UtilPokemonInfo;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class SpawnTimesTransformer implements Transformer {

    private final EnumSpecies pokemon;

    public static SpawnTimesTransformer of(EnumSpecies species) {
        return new SpawnTimesTransformer(species);
    }

    public SpawnTimesTransformer(EnumSpecies pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String transformName(String name) {
        return name.replace("%spawn_times%",  String.join(", ",
                                                          UtilPokemonInfo.getSpawnTimes(this.pokemon.getBaseStats())));
    }
}
