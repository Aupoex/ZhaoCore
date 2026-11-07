package com.example.zhaocore.potion;

import com.example.zhaocore.util.DetonationLogic;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class MagicFireDetonationEffect extends MobEffect {

    public MagicFireDetonationEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF5500);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amp) {
        super.removeAttributeModifiers(entity, map, amp);

        if (!entity.isAlive()) return;

        int ticks = entity.getRemainingFireTicks();
        if (ticks > 0) {
            entity.setRemainingFireTicks(0);
            DetonationLogic.triggerMagicFireDetonation(entity, amp, ticks, null);
        }
    }
}