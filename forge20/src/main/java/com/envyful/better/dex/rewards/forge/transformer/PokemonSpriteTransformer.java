package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.text.parse.SimplePlaceholder;

public class PokemonSpriteTransformer implements SimplePlaceholder {

    private final String spritePath;

    public PokemonSpriteTransformer(String spritePath) {this.spritePath = spritePath;}

    @Override
    public String replace(String name) {
        return name.replace("%sprite%", this.spritePath);
    }
}

