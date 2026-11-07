package com.example.zhaocore.event;

import com.example.zhaocore.util.DetonationLogic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "zhaocore", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DetonationDeathHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        MobEffect magicDetonation = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("zhaocore", "magic_fire_detonation"));

        if (magicDetonation != null && entity.hasEffect(magicDetonation)) {
            int amp = entity.getEffect(magicDetonation).getAmplifier();
            int ticks = entity.getRemainingFireTicks();

            if (ticks > 0) {
                entity.setRemainingFireTicks(0);
                DetonationLogic.triggerMagicFireDetonation(entity, amp, ticks, event.getSource().getEntity());
            }

            entity.removeEffect(magicDetonation);
        }

        MobEffect bicDetonation = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "detonation"));
        if (bicDetonation != null && entity.hasEffect(bicDetonation)) {
            int amp = entity.getEffect(bicDetonation).getAmplifier();
            DetonationLogic.triggerBicDetonation(entity, amp, event.getSource().getEntity());
            entity.removeEffect(bicDetonation);
        }
    }
}