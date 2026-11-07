package com.example.zhaocore.mixin.tacz;

import com.example.zhaocore.compat.tacz.ApexAttachments;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractGunItem.class, remap = false)
public abstract class AbstractGunItemMixin {
    @Inject(method = "allowAttachmentType", at = @At("HEAD"), cancellable = true)
    private void zhaocore$allowApexSlot(ItemStack gun, AttachmentType type, CallbackInfoReturnable<Boolean> cir) {
        if (ApexAttachments.isApex(type)) {
            IGun iGun = IGun.getIGunOrNull(gun);
            cir.setReturnValue(iGun != null && TimelessAPI.getCommonGunIndex(iGun.getGunId(gun)).isPresent());
        }
    }
}
