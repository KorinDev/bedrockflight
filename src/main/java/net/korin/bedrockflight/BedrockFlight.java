package net.korin.bedrockflight;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.mixin.client.keymapping.KeyMappingAccessor;
import net.korin.bedrockflight.config.BedrockFlightConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedrockFlight implements ModInitializer {
	public static final String MOD_ID = "bedrockflight";
    public static final BedrockFlightConfig CONFIG = BedrockFlightConfig.createAndLoad();

    public static float customFlightSpeedMult = 1.0f;

    public static final SystemToast.SystemToastId SPEED_TOAST = new SystemToast.SystemToastId(2000L);

    private static double lastScrollX = 0;

    private static double lastScrollY = 0;


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        LOGGER.info("Initialized");

        KeyMapping.Category CATEGORY = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(BedrockFlight.MOD_ID, "keybinds")
        );

        KeyMapping increaseFlightSpeed = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.bedrockflight.increase",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_EQUAL,
                        CATEGORY
                )
        );

        KeyMapping decreaseFlightSpeed = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.bedrockflight.decrease",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_MINUS,
                        CATEGORY
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
                    while (increaseFlightSpeed.consumeClick()) {
                        if (client.player != null && canChangeSpeed(client.player)) {
                            customFlightSpeedMult += 0.1f;
                            SystemToast.addOrUpdate(client.getToastManager(),
                                    SPEED_TOAST,
                                    Component.literal("Bedrock Flight"),
                                    Component.literal("Speed: %.2fx".formatted(customFlightSpeedMult)));
                        }
                    }
            while (decreaseFlightSpeed.consumeClick()) {
                if (client.player != null && canChangeSpeed(client.player)) {
                    float newSpeed = BedrockFlight.customFlightSpeedMult - 0.1f;
                    BedrockFlight.customFlightSpeedMult = Math.max(0.0f, newSpeed);
                    SystemToast.addOrUpdate(client.getToastManager(),
                            SPEED_TOAST,
                            Component.literal("Bedrock Flight"),
                            Component.literal("Speed: %.2fx".formatted(customFlightSpeedMult)));
                }
            }
        });



	}

    private boolean canChangeSpeed(Player player) {
        GameType gamemode = player.gameMode();
        if (gamemode == GameType.CREATIVE) {
            return true;
        }

        if (gamemode == GameType.SPECTATOR && CONFIG.spectatorSettings.enabledSpectator()) {
            return true;
        }

        return false;
    }

    public static float getCustomFlightSpeedMult() {
        return customFlightSpeedMult;
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