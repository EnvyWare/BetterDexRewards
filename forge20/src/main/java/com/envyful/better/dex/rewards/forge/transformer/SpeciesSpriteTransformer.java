package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.text.parse.SimplePlaceholder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;

public class SpeciesSpriteTransformer implements SimplePlaceholder {

    private final Species species;

    public static SpeciesSpriteTransformer of(Species species) {
        return new SpeciesSpriteTransformer(species);
    }

    private SpeciesSpriteTransformer(Species species) {this.species = species;}

    @Override
    public String replace(String name) {
        return name.replace("%sprite%", this.species.getDefaultForm().getDefaultGenderProperties().getDefaultPalette().getSprite().toString());
    }
}
