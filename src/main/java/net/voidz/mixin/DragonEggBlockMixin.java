package net.voidz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.voidz.init.DimensionInit;

@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin {

    @Inject(method = "useWithoutItem", at = @At(value = "HEAD"), cancellable = true)
    private void onUseMixin(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> info) {
        if (!world.isClientSide() && world.dimension() == DimensionInit.VOID_WORLD) {
            world.removeBlock(pos, false);
            ItemStack itemStack = new ItemStack(Items.DRAGON_EGG);
            if (!player.getInventory().add(itemStack)) {
                player.drop(itemStack, false);
            }
            info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }

}