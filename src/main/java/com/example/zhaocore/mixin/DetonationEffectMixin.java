package com.example.zhaocore.mixin;

import com.example.zhaocore.util.DetonationLogic;
import dev.shadowsoffire.attributeslib.mobfx.DetonationEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DetonationEffect.class)
public class DetonationEffectMixin {

    @Inject(method = "removeAttributeModifiers", at = @At("HEAD"))
    private void onRemove(LivingEntity entity, AttributeMap map, int amp, CallbackInfo ci) {
        int ticks = entity.getRemainingFireTicks();
        if (ticks > 0) {
            entity.setRemainingFireTicks(0);
        }
    }
}