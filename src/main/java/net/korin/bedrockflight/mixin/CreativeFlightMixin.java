package net.korin.bedrockflight.mixin;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class CreativeFlightMixin {

    private double originalY = 0;

    @Shadow
    @Final
    private Abilities abilities;

    @Shadow
    public abstract Abilities getAbilities();

    @Shadow
    public abstract boolean isCreative();

    @Inject(method = "travel", at = @At("HEAD"))
    private void bedrockCreativeFlight(Vec3 input, CallbackInfo ci) {
        Player player = (Player)(Object)this;

        if (player.getAbilities().flying && player.isCreative() && !player.isInWater()) {
            originalY = player.getDeltaMovement().y;

            player.getAbilities().setFlyingSpeed(0.06f);

            if (input.x == 0 && input.z == 0) {
                player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
            }
        }
    }

    /*@Inject(method = "travel", at = @At("RETURN"))
    private void onTravelEnd(Vec3 input, CallbackInfo ci) {
        Player player = (Player)(Object)this;

        if (player.getAbilities().flying && player.isCreative()) {
            Vec3 currentMotion = player.getDeltaMovement();
            player.setDeltaMovement(currentMotion.x, originalY * 0.6, currentMotion.z);
        }
    }*/
}