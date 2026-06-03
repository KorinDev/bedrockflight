package net.korin.bedrockflight.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.korin.bedrockflight.BedrockFlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(method = "onScroll", at = @At("TAIL"))
    private void onScrollTail(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        boolean discreteScroll = client.options.discreteMouseScroll().get();
        double scrollSensitivity = client.options.mouseWheelSensitivity().get();

        double scaledXOffset = (discreteScroll ? Math.signum(xoffset) : xoffset) * scrollSensitivity;
        double scaledYOffset = (discreteScroll ? Math.signum(yoffset) : yoffset) * scrollSensitivity;

        BedrockFlight.setScroll(scaledXOffset, scaledYOffset);
    }
}
