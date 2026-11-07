package com.example.zhaocore.mixin;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import mod.chloeprime.gunsmithlib.api.common.GunAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AttributeSetEffect.class)
public class AttributeSetEffectMixin {

    @ModifyArg(
            method = "update",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttribute(Lnet/minecraft/world/entity/ai/attributes/Attribute;)Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;"),
            index = 0
    )
    private Attribute redirectAttributeTarget(Attribute originalAttribute) {
        if (originalAttribute == L2DamageTracker.CRIT_RATE.get()) {
            return ALObjects.Attributes.CRIT_CHANCE.get();
        }
        if (originalAttribute == L2DamageTracker.CRIT_DMG.get()) {
            return ALObjects.Attributes.CRIT_DAMAGE.get();
        }
        if (originalAttribute == L2DamageTracker.BOW_STRENGTH.get()) {
            return GunAttributes.BULLET_DAMAGE.get();
        }
        return originalAttribute;
    }

    @Redirect(
            method = "update",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addTransientModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V")
    )
    private void redirectAddTransientModifier(AttributeInstance instance, AttributeModifier originalModifier) {
        Attribute attribute = instance.getAttribute();

        if (attribute == ALObjects.Attributes.CRIT_CHANCE.get() ||
                attribute == ALObjects.Attributes.CRIT_DAMAGE.get() ||
                attribute == GunAttributes.BULLET_DAMAGE.get()) {

            double value = originalModifier.getAmount();

            AttributeModifier multiplicativeModifier = new AttributeModifier(
                    originalModifier.getId(),
                    originalModifier.getName(),
                    value,
                    AttributeModifier.Operation.MULTIPLY_BASE
            );

            instance.addTransientModifier(multiplicativeModifier);
        } else {
            instance.addTransientModifier(originalModifier);
        }
    }

}