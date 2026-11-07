package com.example.zhaocore.mixin;

import com.example.zhaocore.HitSoundConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomMoveControl")
public class PhantomMoveControlMixin {

    @ModifyConstant(method = "tick()V", constant = @Constant(floatValue = 4.0F))
    private float modifyTurnSpeed(float originalTurnSpeed) {
        return HitSoundConfig.TURN_SPEED.get().floatValue();
    }

    @ModifyConstant(method = "tick()V", constant = @Constant(floatValue = 1.8F))
    private float modifySwoopSpeed(float originalSwoopSpeed) {
        return HitSoundConfig.SWOOP_SPEED.get().floatValue();
    }

    @ModifyConstant(method = "tick()V", constant = @Constant(floatValue = 0.2F))
    private float modifyNormalSpeed(float originalNormalSpeed) {
        return HitSoundConfig.NORMAL_SPEED.get().floatValue();
    }
}