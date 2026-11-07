package com.example.zhaocore.mixin;

import com.example.zhaocore.ModRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(targets = "net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder")
public abstract class AttributeSupplierBuilderMixin {

    @Shadow @Final private Map<Attribute, AttributeInstance> builder;

    @Shadow public abstract AttributeSupplier.Builder add(Attribute p_22267_);

    @Inject(method = "build", at = @At("HEAD"))
    private void onBuild(CallbackInfoReturnable<AttributeSupplier> cir) {
        if (this.builder.containsKey(Attributes.FOLLOW_RANGE)) {

            this.add(ModRegistry.RANK);
            this.add(ModRegistry.INTENSITY);

        }
    }
}