package net.korin.bedrockflight.mixin;

import net.korin.bedrockflight.BedrockFlight;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;

@Mixin(Player.class)
public abstract class CreativeFlightMixin {


    @Shadow
    public abstract void travel(Vec3 input);

    @Shadow
    public abstract float getAttackStrengthScale(float a);

    @Inject(method = "travel", at = @At("TAIL"))
    private void bedrockCreativeFlight(Vec3 input, CallbackInfo ci) {
        Player player = (Player)(Object)this;

        if (player.getAbilities().flying && player.isCreative() && player.isLocalPlayer()) {

            float speed = BedrockFlight.CONFIG.flyingSpeed();
            float decel = BedrockFlight.CONFIG.decelerationFactor();
            float threshold = BedrockFlight.CONFIG.stopThreshold();

            player.getAbilities().setFlyingSpeed(speed);

            if (input.x == 0 && input.z == 0) {
                Vec3 current = player.getDeltaMovement();

                double newX = current.x * decel;
                double newZ = current.z * decel;

                if (Math.abs(newX) < threshold) newX = 0;
                if (Math.abs(newZ) < threshold) newZ = 0;

                player.setDeltaMovement(newX, current.y, newZ);
            }
        }
    }
}