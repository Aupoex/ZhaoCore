package com.example.zhaocore.mixin.tacz;

import com.example.zhaocore.compat.tacz.ApexAttachments;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.client.model.BedrockGunModel;
import com.tacz.guns.client.model.bedrock.BedrockPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumMap;
import java.util.List;

@Mixin(value = BedrockGunModel.class, remap = false)
public abstract class BedrockGunModelMixin {
    @Shadow @Final
    protected EnumMap<AttachmentType, List<BedrockPart>> refitAttachmentViewPath;

    @Inject(method = "getRefitAttachmentViewPath", at = @At("HEAD"), cancellable = true)
    private void zhaocore$useOverviewForApex(AttachmentType type, CallbackInfoReturnable<List<BedrockPart>> cir) {
        if (ApexAttachments.isApex(type)) {
            cir.setReturnValue(refitAttachmentViewPath.get(AttachmentType.NONE));
        }
    }
}
