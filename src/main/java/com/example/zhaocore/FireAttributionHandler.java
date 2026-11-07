package com.example.zhaocore;

import dev.xkmc.l2damagetracker.contents.attributes.WrappedAttribute;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = ZhaoCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireAttributionHandler {

    private static final Map<LivingEntity, UUID> FIRE_SOURCE_MAP = new WeakHashMap<>();

    @SubscribeEvent
    public static void onAttachFireSource(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            FIRE_SOURCE_MAP.put(event.getEntity(), player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onFireHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();

        if (event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {

            UUID attackerUUID = FIRE_SOURCE_MAP.get(victim);
            if (attackerUUID != null) {
                Player attacker = victim.level().getPlayerByUUID(attackerUUID);

                if (attacker != null) {
                    WrappedAttribute fireAttr = L2DamageTracker.FIRE_FACTOR.get();
                    double multiplier = fireAttr.getWrappedValue(attacker);

                    if (multiplier != 1.0) {
                        event.setAmount((float) (event.getAmount() * multiplier));
                    }
                }
            }
        }
    }
}
