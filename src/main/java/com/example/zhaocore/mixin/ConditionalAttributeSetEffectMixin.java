package com.example.zhaocore.mixin;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import mod.chloeprime.gunsmithlib.api.common.GunAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.function.Supplier;


@Mixin(AbstractConditionalAttributeSetEffect.class)
public class ConditionalAttributeSetEffectMixin {

    @Redirect(
            method = {
                    "addAttributes",
                    "getData(Ldev/xkmc/l2artifacts/content/config/ArtifactSetConfig$Entry;)Ldev/xkmc/l2artifacts/content/effects/attribute/AttributeSetData;",
                    "getDetailedDescription"
            },
            at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;"),
            remap = false
    )
    private Object redirectAttributeSupplier(Supplier<Attribute> supplier) {
        Attribute originalAttribute = supplier.get();

        String className = this.getClass().getName();
        boolean isTargetEffect = className.equals("dev.xkmc.l2artifacts.content.effects.v4.LuckAttackEffect") ||
                className.equals("dev.xkmc.l2artifacts.content.effects.v4.LongShooterEffect") ||
                className.equals("dev.xkmc.l2artifacts.content.effects.v4.LongShooterPersistentEffect");

        if (isTargetEffect) {
            if (originalAttribute == L2DamageTracker.CRIT_RATE.get()) return ALObjects.Attributes.CRIT_CHANCE.get();
            if (originalAttribute == L2DamageTracker.CRIT_DMG.get()) return ALObjects.Attributes.CRIT_DAMAGE.get();
            if (originalAttribute == L2DamageTracker.BOW_STRENGTH.get()) return GunAttributes.BULLET_DAMAGE.get();
        }

        return originalAttribute;
    }

    @Redirect(
            method = "addAttributes",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addTransientModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V")
    )
    private void redirectConditionalAddModifier(AttributeInstance instance, AttributeModifier originalModifier) {

        String className = this.getClass().getName();
        boolean isTargetEffect = className.equals("dev.xkmc.l2artifacts.content.effects.v4.LuckAttackEffect") ||
                className.equals("dev.xkmc.l2artifacts.content.effects.v4.LongShooterEffect") ||
                className.equals("dev.xkmc.l2artifacts.content.effects.v4.LongShooterPersistentEffect");

        if (isTargetEffect) {
            Attribute attribute = instance.getAttribute();

            if (attribute == ALObjects.Attributes.CRIT_CHANCE.get() ||
                    attribute == ALObjects.Attributes.CRIT_DAMAGE.get() ||
                    attribute == GunAttributes.BULLET_DAMAGE.get()) {

                AttributeModifier multiplicativeModifier = new AttributeModifier(
                        originalModifier.getId(),
                        originalModifier.getName(),
                        originalModifier.getAmount(),
                        AttributeModifier.Operation.MULTIPLY_BASE
                );

                instance.addTransientModifier(multiplicativeModifier);
                return;
            }
        }

        instance.addTransientModifier(originalModifier);
    }
}