package com.example.zhaocore;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.example.zhaocore.potion.*;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ZhaoCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ZhaoCore.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ZhaoCore.MODID);


    public static final RegistryObject<Attribute> FIRE_DAMAGE_RESISTANCE = ATTRIBUTES.register("fire_damage_resistance",
            () -> new RangedAttribute("attribute.name.zhaocore.fire_damage_resistance", 1.0D, -1024.0D, 1024.0D).setSyncable(true));


    public static final RegistryObject<Attribute> ENDER_REACH = ATTRIBUTES.register("ender_reach",
            () -> new RangedAttribute("attribute.name.zhaocore.ender_reach", 0.0D, 0.0D, 10.0D)
                    .setSyncable(true));

    public static final RegistryObject<Attribute> PERFECT_BLOCK = ATTRIBUTES.register("perfect_block",
            () -> new RangedAttribute("attribute.name.zhaocore.perfect_block", 0.0D, 0.0D, 10.0D)
                    .setSyncable(true));


    public static final Attribute RANK = new RangedAttribute("attribute.name.zhaocore.rank", 0.0D, 0.0D, 1024.0D).setSyncable(true);
    public static final Attribute INTENSITY = new RangedAttribute("attribute.name.zhaocore.intensity", 0.0D, 0.0D, 1024.0D).setSyncable(true);



    public static final RegistryObject<MobEffect> MAGIC_FIRE_DETONATION = EFFECTS.register("magic_fire_detonation", MagicFireDetonationEffect::new);
    public static final RegistryObject<MobEffect> TACTICAL_VISOR_EFFECT = EFFECTS.register("tactical_visor", TacticalVisorEffect::new);
    @SubscribeEvent
    public static void onAttributeModify(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, PERFECT_BLOCK.get());

        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {
            event.add(entityType, FIRE_DAMAGE_RESISTANCE.get());
        }
    }
    public static class ModMobEffect extends MobEffect {
        public ModMobEffect(MobEffectCategory category, int color) {
            super(category, color);
        }
    }
    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ATTRIBUTE)) {
            event.register(Registries.ATTRIBUTE, helper -> {
                helper.register(new ResourceLocation("zhaocore", "rank"), RANK);
                helper.register(new ResourceLocation("zhaocore", "intensity"), INTENSITY);
            });
        }
    }
}
