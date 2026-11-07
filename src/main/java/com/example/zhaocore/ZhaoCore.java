package com.example.zhaocore;

import com.example.zhaocore.network.ModNetworking;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ZhaoCore.MODID)
public class ZhaoCore {
    public static final String MODID = "zhaocore";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ZhaoCore() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRegistry.ATTRIBUTES.register(modEventBus);
        ModRegistry.EFFECTS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HitSoundConfig.COMMON_CONFIG);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetworking.register();
        event.enqueueWork(() -> {
            MobEffects.FIRE_RESISTANCE.addAttributeModifier(
                    ModRegistry.FIRE_DAMAGE_RESISTANCE.get(),
                    "55e1dbba-10e8-4cb1-97b7-5f726cb3b9a5",
                    0.2D,
                    AttributeModifier.Operation.ADDITION
            );
        });
    }
}