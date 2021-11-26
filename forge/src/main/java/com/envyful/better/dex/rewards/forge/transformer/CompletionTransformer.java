package com.envyful.better.dex.rewards.forge.transformer;

import com.envyful.api.gui.Transformer;

public class CompletionTransformer implements Transformer {

    private final double percentage;

    public static CompletionTransformer of(double percentage) {
        return new CompletionTransformer(percentage);
    }

    private CompletionTransformer(double percentage) {this.percentage = percentage;}

    @Override
    public String transformName(String name) {
        return name.replace(
                "%percentage%",
                String.format("%.2f", percentage) + "%"
        );
    }
}
