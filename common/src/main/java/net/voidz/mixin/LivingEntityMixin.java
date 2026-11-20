package net.voidz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.LivingEntity;
import net.voidz.init.EventInit;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "canGlide", at = @At("HEAD"), cancellable = true)
    private void canGlideMixin(CallbackInfoReturnable<Boolean> info) {
        if (!EventInit.canGlide((LivingEntity) (Object) this)) {
            info.setReturnValue(false);
        }
    }

}
