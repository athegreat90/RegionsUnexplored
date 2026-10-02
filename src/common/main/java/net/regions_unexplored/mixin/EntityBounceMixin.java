package net.regions_unexplored.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.regions_unexplored.block.type.aquatic.GiantLilyPadBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityBounceMixin {
    @Inject(method = "getBlockBounciness", at = @At("HEAD"), cancellable = true)
    private void lilyPadBounciness(Block block, CallbackInfoReturnable<Double> callback) {
        if (block instanceof GiantLilyPadBlock) {
            Entity entity = (Entity) (Object) this;
            double restitution = entity.getType() == EntityTypes.FROG || entity.isSuppressingBounce() || entity.getDeltaMovement().y >= -0.7
                ? 0 : entity instanceof LivingEntity ? 1 : 0.8;
            callback.setReturnValue(restitution);
        }
    }
}
