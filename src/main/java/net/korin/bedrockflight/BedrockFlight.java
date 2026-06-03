package net.korin.bedrockflight;

import net.fabricmc.api.ModInitializer;

import net.korin.bedrockflight.config.BedrockFlightConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedrockFlight implements ModInitializer {
	public static final String MOD_ID = "bedrockflight";
    public static final BedrockFlightConfig CONFIG = BedrockFlightConfig.createAndLoad();

    private static double lastScrollX = 0;

    private static double lastScrollY = 0;


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        LOGGER.info("Initialized");

	}

    public static void setScroll(double x, double y) {
        lastScrollX = x;
        lastScrollY = y;
    }

    public static double getLastScrollY() {
        return lastScrollY;
    }

    public static double getLastScrollX() {
        return lastScrollX;
    }

    public static void consumeScroll() {
        lastScrollY = 0;
        lastScrollX = 0;
    }

    public static boolean hasScroll() {
        return lastScrollX != 0 || lastScrollY != 0;
    }
}