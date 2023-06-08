package net.voidz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidz.init.DimensionInit;
import net.minecraft.block.DragonEggBlock;

@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin {

    @Inject(method = "onUse", at = @At(value = "HEAD"), cancellable = true)
    private void onUseMixin(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        if (!world.isClient() && world.getRegistryKey() == DimensionInit.VOID_WORLD) {
            world.removeBlock(pos, false);
            ItemStack itemStack = new ItemStack(Items.DRAGON_EGG);
            if (!player.getInventory().insertStack(itemStack)) {
                player.dropItem(itemStack, false);
            }
            info.setReturnValue(ActionResult.success(world.isClient));
        }
    }

}