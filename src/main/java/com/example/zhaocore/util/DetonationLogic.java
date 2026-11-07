package com.example.zhaocore.util;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class DetonationLogic {

    public static Player getResponsiblePlayer(LivingEntity victim, Entity explicitKiller) {
        if (explicitKiller instanceof Player p) return p;
        if (explicitKiller instanceof OwnableEntity ownable && ownable.getOwner() instanceof Player p) return p;

        if (victim.getLastHurtByMob() instanceof Player p) return p;
        if (victim.getLastHurtByMob() instanceof OwnableEntity ownable && ownable.getOwner() instanceof Player p) return p;

        if (victim instanceof Player p) return p;
        return null;
    }

    public static void triggerMagicFireDetonation(LivingEntity entity, int amp, int ticks, Entity explicitKiller) {
        Level level = entity.level();
        if (level.isClientSide()) return;

        Player responsiblePlayer = getResponsiblePlayer(entity, explicitKiller);

        float baseRadius = 3.0F;
        float radiusMultiplier = 1.0F + (amp * 0.5F);
        float finalRadius = baseRadius * radiusMultiplier;

        float baseDamage = (float) ((1 + amp) * ticks) / 14.0F;

        double fireFactor = 1.0D;
        double explosionFactor = 1.0D;
        if (responsiblePlayer != null) {
            fireFactor = L2DamageTracker.FIRE_FACTOR.get().getWrappedValue(responsiblePlayer);
            explosionFactor = L2DamageTracker.EXPLOSION_FACTOR.get().getWrappedValue(responsiblePlayer);
        }

        float finalDamage = (float) (baseDamage * fireFactor * explosionFactor);

        DamageSource dmgSource = (responsiblePlayer != null)
                ? level.damageSources().indirectMagic(entity, responsiblePlayer)
                : level.damageSources().source(ALObjects.DamageTypes.BLEEDING);

        AABB explosionBox = entity.getBoundingBox().inflate(finalRadius);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, explosionBox,
                e -> e.distanceTo(entity) <= finalRadius
        );

        for (LivingEntity target : targets) {
            target.hurt(dmgSource, finalDamage);
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, entity.getX(), entity.getY(), entity.getZ(), 100 * (amp + 1), finalRadius * 0.5, finalRadius * 0.5, finalRadius * 0.5, 0.25D);
            serverLevel.playSound(null, entity, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 1.0F, 1.2F);
        }


    }

    public static void triggerBicDetonation(LivingEntity entity, int amp, Entity explicitKiller) {
        Level level = entity.level();
        if (level.isClientSide()) return;

        Player responsiblePlayer = getResponsiblePlayer(entity, explicitKiller);

        Entity exploder = (responsiblePlayer != null) ? responsiblePlayer : entity;

        double explosionFactor = (responsiblePlayer != null)
                ? L2DamageTracker.EXPLOSION_FACTOR.get().getWrappedValue(responsiblePlayer)
                : 1.0D;

        float bicRadius = 3.0f;

        level.explode(exploder, entity.getX(), entity.getY(), entity.getZ(), bicRadius, Level.ExplosionInteraction.NONE);

    }
}