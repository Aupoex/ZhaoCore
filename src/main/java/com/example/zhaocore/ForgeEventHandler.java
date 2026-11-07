package com.example.zhaocore;

import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.init.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ZhaoCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            LivingEntity entity = event.getEntity();
            AttributeInstance resistanceAttr = entity.getAttribute(ModRegistry.FIRE_DAMAGE_RESISTANCE.get());

            if (resistanceAttr != null && resistanceAttr.getValue() >= 2.0) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            LivingEntity entity = event.getEntity();
            AttributeInstance resistanceAttr = entity.getAttribute(ModRegistry.FIRE_DAMAGE_RESISTANCE.get());

            if (resistanceAttr != null) {
                double resistance = resistanceAttr.getValue();

                if (resistance >= 2.0) {
                    event.setCanceled(true);
                } else {
                    float multiplier = (float) Math.max(0.0D, 2.0D - resistance);
                    event.setAmount(event.getAmount() * multiplier);
                }
            }
        }
    }



    @SubscribeEvent
    public static void onGunHit(EntityHurtByGunEvent.Post event) {

        if (event.getLogicalSide().isClient()) {
            return;
        }

        if (!(event.getAttacker() instanceof ServerPlayer serverPlayer)) return;
        if (!(event.getHurtEntity() instanceof LivingEntity target)) return;

        AttributeInstance resistanceInst = target.getAttribute(ModAttributes.BULLET_RESISTANCE.get());

        if (resistanceInst != null) {
            double resistance = resistanceInst.getValue();

            String soundLocationStr = "";
            if (resistance < 0.2) {
                soundLocationStr = HitSoundConfig.SOUND_TIER_1.get();
            } else if (resistance < 0.4) {
                soundLocationStr = HitSoundConfig.SOUND_TIER_2.get();
            } else if (resistance < 0.6) {
                soundLocationStr = HitSoundConfig.SOUND_TIER_3.get();
            } else if (resistance < 0.8) {
                soundLocationStr = HitSoundConfig.SOUND_TIER_4.get();
            } else if (resistance < 0.99) {
                soundLocationStr = HitSoundConfig.SOUND_TIER_5.get();
            }else {
                soundLocationStr = HitSoundConfig.SOUND_TIER_6.get();
            }

            if (!soundLocationStr.isEmpty()) {
                ResourceLocation soundId = new ResourceLocation(soundLocationStr);

                SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(soundId);
                Holder<SoundEvent> soundHolder = Holder.direct(soundEvent);

                serverPlayer.connection.send(new ClientboundSoundPacket(
                        soundHolder,
                        SoundSource.PLAYERS,
                        serverPlayer.getX(),
                        serverPlayer.getY(),
                        serverPlayer.getZ(),
                        1.0F,
                        1.0F,
                        serverPlayer.getRandom().nextLong()
                ));

                if (resistance >= 0.99) {
                    com.example.zhaocore.network.ModNetworking.sendToPlayer(
                            new com.example.zhaocore.network.ArmorHitPacket(),
                            serverPlayer
                    );
                }
            }
        }
    }




}
