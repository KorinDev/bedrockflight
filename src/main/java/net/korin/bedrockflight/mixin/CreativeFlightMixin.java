package net.korin.bedrockflight.mixin;

import net.korin.bedrockflight.BedrockFlight;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class CreativeFlightMixin {



    @Inject(method = "travel", at = @At("TAIL"))
    private void bedrockCreativeFlight(Vec3 input, CallbackInfo ci) {
        if (!BedrockFlight.CONFIG.enabled()) {
            return; // Cancel if mod is disabled.
        }

        Player player = (Player)(Object)this;

        if (player.gameMode() == GameType.SURVIVAL || player.gameMode() == GameType.ADVENTURE) {
            return; // Cancel if in survival / adventure mode.
        }

        if (player.gameMode() == GameType.SPECTATOR && !BedrockFlight.CONFIG.enabledSpectator()) {
            return; // Cancel if in spectator mode, unless "enabledSpectator" is set to true. false by default.
        }

        if (player.getAbilities().flying && player.isLocalPlayer()) { // Confirm if is "this" player and if can fly.

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
        } else {
            player.getAbilities().setFlyingSpeed(0.05F); // Reset flight speed back to vanilla 0.05F
        }
    }
}