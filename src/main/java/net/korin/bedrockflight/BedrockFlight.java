package net.korin.bedrockflight;

import net.fabricmc.api.ModInitializer;

import net.korin.bedrockflight.config.BedrockFlightConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedrockFlight implements ModInitializer {
	public static final String MOD_ID = "bedrockflight";
    public static final BedrockFlightConfig CONFIG = BedrockFlightConfig.createAndLoad();


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        LOGGER.info("Initialized");

	}
}