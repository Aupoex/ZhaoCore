package com.example.zhaocore.mixin.tacz;

import com.example.zhaocore.compat.tacz.AttachmentTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.resource.CommonAssetsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CommonAssetsManager.class, remap = false)
public abstract class CommonAssetsManagerMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;"))
    private static Gson zhaocore$registerApexType(GsonBuilder builder) {
        return builder.registerTypeAdapter(AttachmentType.class, new AttachmentTypeAdapter()).create();
    }
}
