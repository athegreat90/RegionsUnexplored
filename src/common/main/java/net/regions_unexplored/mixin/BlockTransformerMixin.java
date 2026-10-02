package net.regions_unexplored.mixin;

import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.item.context.UseOnContext;
import net.regions_unexplored.util.BlockCompatUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockTransformer.class)
public class BlockTransformerMixin {
    @Inject(method = "transformBlock", at = @At("HEAD"), cancellable = true)
    private void transformRUBlocks(UseOnContext context, CallbackInfoReturnable<InteractionResult> callback) {
        var transformer = context.getItemInHand().get(DataComponents.BLOCK_TRANSFORMER);
        // The supplemental transformer's recursive call must not re-enter this hook.
        if (transformer == null || transformer.value() != (Object) this) return;
        BlockTransformer extra;
        if (transformer.is(BlockTransformers.AXE)) {
            extra = BlockCompatUtil.axeTransformer();
        } else if (transformer.is(BlockTransformers.SHOVEL)) {
            extra = BlockCompatUtil.shovelTransformer();
        } else {
            return;
        }
        InteractionResult result = extra.transformBlock(context);
        if (result.consumesAction()) callback.setReturnValue(result);
    }
}
