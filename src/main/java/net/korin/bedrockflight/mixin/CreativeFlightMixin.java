package net.korin.bedrockflight.mixin;

import net.korin.bedrockflight.BedrockFlight;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class CreativeFlightMixin {

    @Unique
    private float spectatorAddSpeed = 0.0f;



    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void travelHEAD(Vec3 input, CallbackInfo ci) {
        if (!BedrockFlight.CONFIG.enabled()) {
            return; // Cancel if mod is disabled.
        }
        Player player = (Player)(Object)this;


        if (!BedrockFlight.CONFIG.legacySprintFlight.enabled()) return;




        if (!player.getAbilities().flying || !Minecraft.getInstance().options.keySprint.isDown()) return;

        Vec3 lookVec = player.getLookAngle();
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        float speed = BedrockFlight.CONFIG.flyingSpeed() * 8;

        Vec3 movement = new Vec3(0, 0, 0);

        if (input.z > 0) movement = movement.add(lookVec.scale(input.z));
        if (input.z < 0) movement = movement.subtract(lookVec.scale(-input.z));

        if (input.x > 0) movement = movement.add(rightVec.scale(-input.x));
        if (input.x < 0) movement = movement.subtract(rightVec.scale(input.x));

        if (input.y > 0) movement = movement.add(0, speed, 0);
        if (input.y < 0) movement = movement.add(0, -speed, 0);

        movement = movement.normalize().scale(speed);

        player.setDeltaMovement(movement);
        player.move(MoverType.SELF, player.getDeltaMovement());
        return;
    }



    @Inject(method = "travel", at = @At("TAIL"))
    private void bedrockCreativeFlight(Vec3 input, CallbackInfo ci) {
        if (!BedrockFlight.CONFIG.enabled()) {
            return; // Cancel if mod is disabled.
        }

        Player player = (Player)(Object)this;

        if (player.gameMode() == GameType.SURVIVAL || player.gameMode() == GameType.ADVENTURE) {
            return; // Cancel if in survival / adventure mode.
        }

        if (player.gameMode() == GameType.SPECTATOR && !BedrockFlight.CONFIG.spectatorSettings.enabledSpectator()) {
            return; // Cancel if in spectator mode, unless "enabledSpectator" is set to true. false by default.
        }

        if (player.getAbilities().flying && player.isLocalPlayer()) { // Confirm if is "this" player and if they can fly.

            float speed = BedrockFlight.CONFIG.flyingSpeed();
            float decel = BedrockFlight.CONFIG.decelerationFactor();
            float threshold = BedrockFlight.CONFIG.stopThreshold();



            if (player.gameMode() == GameType.SPECTATOR && BedrockFlight.CONFIG.spectatorSettings.enabledSpectator() && BedrockFlight.CONFIG.spectatorSettings.spectatorAdjustSpeedViaScroll()){
                if (BedrockFlight.hasScroll()) {
                    if (spectatorAddSpeed >= 4.0f){
                        spectatorAddSpeed = 4.0f;
                    }
                    if (spectatorAddSpeed < 0.0f) {
                        spectatorAddSpeed = 0.0f;
                    }
                    spectatorAddSpeed += (float) BedrockFlight.getLastScrollY() * BedrockFlight.CONFIG.spectatorSettings.spectatorScrollSensitivity();

                }
                player.getAbilities().setFlyingSpeed((speed + spectatorAddSpeed) * BedrockFlight.getCustomFlightSpeedMult());
                BedrockFlight.consumeScroll();
            } else {
                spectatorAddSpeed = 0.0f;
                player.getAbilities().setFlyingSpeed(speed * BedrockFlight.getCustomFlightSpeedMult());
            }



            if (input.x == 0 && input.z == 0) {
                Vec3 current = player.getDeltaMovement();

                double newX = current.x * decel;
                double newZ = current.z * decel;

                if (Math.abs(newX) < threshold) newX = 0;
                if (Math.abs(newZ) < threshold) newZ = 0;

                player.setDeltaMovement(newX, current.y, newZ);
            }

            if (input.y == 0) {
                Vec3 current = player.getDeltaMovement();

                double newY = current.y * decel;

                if (Math.abs(newY) < threshold) newY = 0;

                player.setDeltaMovement(current.x, newY * BedrockFlight.CONFIG.verticalSpeedBoost(), current.z);
            }

        } else {
            player.getAbilities().setFlyingSpeed(0.05F); // Reset flight speed back to vanilla 0.05F
        }
    }
}