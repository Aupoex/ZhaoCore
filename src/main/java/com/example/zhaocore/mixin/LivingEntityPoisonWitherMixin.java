package com.example.zhaocore.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityPoisonWitherMixin {

    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    private void onEffectAddedInstantly(MobEffectInstance instance, Entity source, CallbackInfo ci) {
        fireResistance$triggerInstantDamage((LivingEntity) (Object) this, instance);
    }

    @Inject(method = "onEffectUpdated", at = @At("HEAD"))
    private void onEffectUpdatedInstantly(MobEffectInstance instance, boolean forced, Entity source, CallbackInfo ci) {
        fireResistance$triggerInstantDamage((LivingEntity) (Object) this, instance);
    }

    @Unique
    private void fireResistance$triggerInstantDamage(LivingEntity entity, MobEffectInstance instance) {
        if (entity.level().isClientSide()) return;

        MobEffect effect = instance.getEffect();
        int amplifier = instance.getAmplifier();
        int duration = instance.getDuration();
        float damage = 1.0F + amplifier;

        if (effect == MobEffects.POISON) {
            if (duration % 25 == 0) return;

            if (entity.getHealth() > 1.0F) {
                forceHurt(entity, entity.damageSources().magic(), damage);
            }
        } else if (effect == MobEffects.WITHER) {
            if (duration % 40 == 0) return;

            forceHurt(entity, entity.damageSources().wither(), damage);
        }
    }

    @Unique
    private void forceHurt(LivingEntity entity, DamageSource source, float damage) {
        int oldInvul = entity.invulnerableTime;
        entity.invulnerableTime = 0;

        entity.hurt(source, damage);

        entity.invulnerableTime = oldInvul;
    }
}