package com.example.zhaocore.mixin.tacz;

import com.tacz.guns.api.item.attachment.AttachmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = AttachmentType.class, remap = false)
public interface AttachmentTypeInvoker {
    @Invoker("<init>")
    static AttachmentType zhaocore$create(String name, int ordinal) {
        throw new AssertionError("Mixin constructor invoker was not applied");
    }
}
