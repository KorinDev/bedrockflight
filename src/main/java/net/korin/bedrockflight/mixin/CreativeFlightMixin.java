package net.korin.bedrockflight.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;

@Mixin(Player.class)
public abstract class CreativeFlightMixin {

    private static final float DECELERATION_FACTOR = 0.65f;


    @Inject(method = "travel", at = @At("TAIL"))
    private void bedrockCreativeFlight(Vec3 input, CallbackInfo ci) {
        Player player = (Player)(Object)this;

        if (player.getAbilities().flying && player.isCreative() && player.isLocalPlayer()) {
            player.getAbilities().setFlyingSpeed(0.06f);

            if (input.x == 0 && input.z == 0) {
                Vec3 current = player.getDeltaMovement();

                double newX = current.x * DECELERATION_FACTOR;
                double newZ = current.z * DECELERATION_FACTOR;

                if (Math.abs(newX) < 0.001) newX = 0;
                if (Math.abs(newZ) < 0.001) newZ = 0;

                player.setDeltaMovement(newX, current.y, newZ);
            }
        }
    }
}