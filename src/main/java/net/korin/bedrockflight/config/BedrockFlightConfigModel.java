package net.korin.bedrockflight.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;

@Modmenu(modId = "bedrockflight")
@Config(name = "bedrockflight", wrapperName = "BedrockFlightConfig")
public class BedrockFlightConfigModel {

    public boolean enabled = true;

    public boolean enabledSpectator = false;

    @RangeConstraint(min = 0.01, max = 0.1)
    public float flyingSpeed = 0.06f;

    @RangeConstraint(min = 0.1, max = 1.0)
    public float decelerationFactor = 0.65f;

    @RangeConstraint(min = 0.0, max = 0.002)
    public float stopThreshold = 0.001f;
}