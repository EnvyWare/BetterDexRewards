package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;

public class PokemonSpriteTransformer implements Transformer {

    private final String spritePath;

    public PokemonSpriteTransformer(String spritePath) {this.spritePath = spritePath;}

    @Override
    public String transformName(String name) {
        return name.replace("%sprite%", this.spritePath);
    }
}

