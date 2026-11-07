package com.example.zhaocore.potion;

import mod.chloeprime.gunsmithlib.common.gunpack_extension.shared.fire_control.FireControlAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class TacticalVisorEffect extends MobEffect {
    private static final UUID RANGE_MODIFIER_UUID = UUID.fromString("6b72e988-bd2e-46e0-9249-0d686ebef8fd");
    private static final UUID ANGLE_MODIFIER_UUID = UUID.fromString("f284903a-bff2-43b5-81f6-2d603070245c");

    public TacticalVisorEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFCC);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        double targetRange = 30.0 * Math.pow(2, amplifier);

        double targetAngle = Math.min(360.0, 80.0 * Math.pow(2, amplifier));

        AttributeInstance rangeAttr = attributeMap.getInstance(FireControlAttributes.AIM_LOCK_RANGE.get());
        if (rangeAttr != null) {
            rangeAttr.removeModifier(RANGE_MODIFIER_UUID);
            rangeAttr.addTransientModifier(new AttributeModifier(
                    RANGE_MODIFIER_UUID,
                    "Potion aim lock range",
                    targetRange,
                    AttributeModifier.Operation.ADDITION
            ));
        }

        AttributeInstance angleAttr = attributeMap.getInstance(FireControlAttributes.AIM_LOCK_ANGLE.get());
        if (angleAttr != null) {
            angleAttr.removeModifier(ANGLE_MODIFIER_UUID);
            angleAttr.addTransientModifier(new AttributeModifier(
                    ANGLE_MODIFIER_UUID,
                    "Potion aim lock angle",
                    targetAngle,
                    AttributeModifier.Operation.ADDITION
            ));
        }

        super.addAttributeModifiers(entity, attributeMap, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        AttributeInstance rangeAttr = attributeMap.getInstance(FireControlAttributes.AIM_LOCK_RANGE.get());
        if (rangeAttr != null) {
            rangeAttr.removeModifier(RANGE_MODIFIER_UUID);
        }

        AttributeInstance angleAttr = attributeMap.getInstance(FireControlAttributes.AIM_LOCK_ANGLE.get());
        if (angleAttr != null) {
            angleAttr.removeModifier(ANGLE_MODIFIER_UUID);
        }

        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }
}
