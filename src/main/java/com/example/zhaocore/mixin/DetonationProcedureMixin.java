package com.example.zhaocore.mixin;

import com.example.zhaocore.util.DetonationLogic;
import net.mcreator.borninchaosv.procedures.DetonationKazhdyiTikVoVriemiaEffiektaProcedure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DetonationKazhdyiTikVoVriemiaEffiektaProcedure.class)
public class DetonationProcedureMixin {

    @Inject(method = "execute", at = @At("HEAD"), remap = false, cancellable = true)
    private static void onExecute(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity livEnt)) return;

        MobEffect bicDetonation = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "detonation"));
        if (bicDetonation != null && livEnt.hasEffect(bicDetonation)) {
            int duration = livEnt.getEffect(bicDetonation).getDuration();

            if (duration <= 20) {
                int amp = livEnt.getEffect(bicDetonation).getAmplifier();

                DetonationLogic.triggerBicDetonation(livEnt, amp, null);
                livEnt.removeEffect(bicDetonation);

                MobEffect blockBreak = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "block_break"));
                if (blockBreak != null && !livEnt.level().isClientSide()) {
                    livEnt.addEffect(new MobEffectInstance(blockBreak, 60, 0, false, false));
                }

                ci.cancel();
            }
        }
    }
}