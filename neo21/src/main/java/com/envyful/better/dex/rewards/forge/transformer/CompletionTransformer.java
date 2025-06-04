package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.text.parse.SimplePlaceholder;

public class CompletionTransformer implements SimplePlaceholder {

    private final double percentage;

    public static CompletionTransformer of(double percentage) {
        return new CompletionTransformer(percentage);
    }

    private CompletionTransformer(double percentage) {this.percentage = percentage;}

    @Override
    public String replace(String name) {
        return name.replace(
                "%percentage%",
                String.format("%.2f", percentage) + "%"
        );
    }
}
