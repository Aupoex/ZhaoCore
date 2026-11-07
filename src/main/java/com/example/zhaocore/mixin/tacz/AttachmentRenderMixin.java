package com.example.zhaocore.mixin.tacz;

import com.example.zhaocore.compat.tacz.ApexAttachments;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tacz.guns.api.item.IAttachment;
import com.tacz.guns.client.model.functional.AttachmentRender;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AttachmentRender.class, remap = false)
public abstract class AttachmentRenderMixin {
    @Inject(method = "renderAttachment", at = @At("HEAD"), cancellable = true)
    private static void zhaocore$skipApexModel(ItemStack attachment, ItemStack gun, PoseStack poses,
                                             ItemDisplayContext context, int light, int overlay, CallbackInfo ci) {
        IAttachment iAttachment = IAttachment.getIAttachmentOrNull(attachment);
        if (iAttachment != null && ApexAttachments.isApex(iAttachment.getType(attachment))) {
            ci.cancel();
        }
    }
}
