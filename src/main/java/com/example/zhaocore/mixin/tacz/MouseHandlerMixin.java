package com.example.zhaocore.mixin.tacz;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MouseHandler.class, priority = 900)
public abstract class MouseHandlerMixin {
    @Dynamic("Added by com.tacz.guns.mixin.client.MouseHandlerMixin")
    @Inject(method = "getCrawlPitch", at = @At("HEAD"), cancellable = true, remap = false)
    private static void zhaocore$unrestrictedCrawlPitch(LocalPlayer player, double pitch, double sensitivity,
                                                      CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(pitch * sensitivity);
    }
}
