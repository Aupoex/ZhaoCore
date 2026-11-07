package com.example.zhaocore.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public class MobEffectMixin {

    @Inject(method = "isDurationEffectTick", at = @At("HEAD"), cancellable = true)
    private void fixedFrequencyTick(int duration, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        MobEffect effect = (MobEffect) (Object) this;
        if (effect == MobEffects.POISON) {
            cir.setReturnValue(duration % 25 == 0);
        } else if (effect == MobEffects.WITHER) {
            cir.setReturnValue(duration % 40 == 0);
        }
    }

    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    private void scalingDamageTick(LivingEntity entity, int amplifier, CallbackInfo ci) {
        MobEffect effect = (MobEffect) (Object) this;

        float damage = 1.0F + amplifier;

        if (effect == MobEffects.POISON) {
            if (entity.getHealth() > 1.0F) {
                int oldInvul = entity.invulnerableTime;
                entity.invulnerableTime = 0;
                entity.hurt(entity.damageSources().magic(), damage);
                entity.invulnerableTime = oldInvul;
            }
            ci.cancel();
        } else if (effect == MobEffects.WITHER) {
            int oldInvul = entity.invulnerableTime;
            entity.invulnerableTime = 0;
            entity.hurt(entity.damageSources().wither(), damage);
            entity.invulnerableTime = oldInvul;
            ci.cancel();
        }
    }
}