package com.example.zhaocore.client;

import com.example.zhaocore.HitSoundConfig;
import com.example.zhaocore.ZhaoCore;
import com.example.zhaocore.network.ClientHitmarkerState;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ZhaoCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HitmarkerOverlay {

    private static final ResourceLocation HITMARKER_ICON = new ResourceLocation(ZhaoCore.MODID, "textures/gui/heavy_armor_hit.png");

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("armor_hitmarker", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {

            int timer = ClientHitmarkerState.showTimer;
            if (timer > 0) {
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;

                int x = centerX + 8 + HitSoundConfig.X_OFFSET.get();
                int y = centerY + 8 + HitSoundConfig.Y_OFFSET.get();

                RenderSystem.enableBlend();

                float alpha = timer / 10.0F;
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

                guiGraphics.blit(HITMARKER_ICON, x, y, 16, 16, 0, 0, 256, 256, 256, 256);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
            }
        });
    }
}