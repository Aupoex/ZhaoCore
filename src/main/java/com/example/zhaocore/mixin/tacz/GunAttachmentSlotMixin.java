package com.example.zhaocore.mixin.tacz;

import com.example.zhaocore.compat.tacz.ApexAttachments;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.client.gui.GunRefitScreen;
import com.tacz.guns.client.gui.components.refit.GunAttachmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GunAttachmentSlot.class, remap = false)
public abstract class GunAttachmentSlotMixin {
    @Shadow @Final
    private AttachmentType type;

    @Unique
    private static final ResourceLocation ZHAOCORE_APEX_ICON =
            new ResourceLocation("zhaocore", "textures/gui/apex_attachments.png");

    @Redirect(method = "renderWidget", remap = true, at = @At(value = "INVOKE", remap = true,
            target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V"))
    private void zhaocore$useLocalApexIcon(GuiGraphics graphics, ResourceLocation texture,
                                         int x, int y, int width, int height, float u, float v,
                                         int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        if (ApexAttachments.isApex(type) && GunRefitScreen.ICONS_TEXTURE.equals(texture)
                && (int) u != GunRefitScreen.ICON_UV_SIZE * 6) {
            graphics.blit(ZHAOCORE_APEX_ICON, x, y, width, height, 0, 0, 64, 64, 64, 64);
        } else {
            graphics.blit(texture, x, y, width, height, u, v,
                    regionWidth, regionHeight, textureWidth, textureHeight);
        }
    }
}
