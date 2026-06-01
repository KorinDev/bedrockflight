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

    @Inject(method = "travel", at = @At("HEAD"))
    private void bedrockCreativeFlight(Vec3 input, CallbackInfo ci) {
        Player player = (Player)(Object)this;

        if (player.getAbilities().flying && player.isCreative() && !player.isInWater()) {
            player.getAbilities().setFlyingSpeed(0.06f);
            if (input.x == 0 && input.z == 0) {
                player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
            }
        }
    }
}
