package com.example.zhaocore.mixin.tacz;

import com.example.zhaocore.compat.tacz.ApexAttachments;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.client.gui.GunRefitScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GunRefitScreen.class, remap = false)
public abstract class GunRefitScreenMixin {
    @Inject(method = "getSlotTextureXOffset", at = @At("HEAD"), cancellable = true)
    private static void zhaocore$apexPlaceholderIcon(ItemStack gun, AttachmentType type, CallbackInfoReturnable<Integer> cir) {
        if (ApexAttachments.isApex(type)) {
            IGun iGun = IGun.getIGunOrNull(gun);
            cir.setReturnValue(iGun != null && iGun.allowAttachmentType(gun, type)
                    ? 0 : GunRefitScreen.ICON_UV_SIZE * 6);
        }
    }
}
