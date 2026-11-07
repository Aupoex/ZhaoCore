package com.example.zhaocore.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin {

    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At("RETURN"))
    private void injectZhaoMobTag(Map<ResourceLocation, ?> entries, CallbackInfoReturnable<Map<ResourceLocation, Collection<Object>>> cir) {
        Map<ResourceLocation, Collection<Object>> tags = cir.getReturnValue();

        ResourceLocation targetTagId = new ResourceLocation("zhaocore", "zhao_mob");

        if (tags.containsKey(new ResourceLocation("minecraft", "skeletons"))) {

            Collection<Object> zhaoMobs = new ArrayList<>();

            if (tags.containsKey(targetTagId)) {
                zhaoMobs.addAll(tags.get(targetTagId));
            }

            for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {

                @SuppressWarnings({"unchecked", "rawtypes"})
                boolean isLivingEntity = DefaultAttributes.hasSupplier((EntityType) entityType);

                if (isLivingEntity) {
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    AttributeSupplier attributes = DefaultAttributes.getSupplier((EntityType) entityType);

                    if (attributes.hasAttribute(Attributes.FOLLOW_RANGE)) {

                        BuiltInRegistries.ENTITY_TYPE.getHolder(entityType.builtInRegistryHolder().key()).ifPresent(holder -> {
                            if (!zhaoMobs.contains(holder)) {
                                zhaoMobs.add(holder);
                            }
                        });

                    }
                }
            }

            tags.put(targetTagId, zhaoMobs);
        }
    }
}