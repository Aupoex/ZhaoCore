package com.example.zhaocore.mixin.tacz;

import com.example.zhaocore.compat.tacz.ApexAttachments;
import com.tacz.guns.api.item.attachment.AttachmentType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = AttachmentType.class, remap = false)
public abstract class AttachmentTypeMixin {
    @Shadow @Final @Mutable
    private static AttachmentType[] $VALUES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void zhaocore$addApexType(CallbackInfo ci) {
        int ordinal = $VALUES.length;
        AttachmentType apex = AttachmentTypeInvoker.zhaocore$create(ApexAttachments.ENUM_NAME, ordinal);
        $VALUES = Arrays.copyOf($VALUES, ordinal + 1);
        $VALUES[ordinal] = apex;
    }
}
