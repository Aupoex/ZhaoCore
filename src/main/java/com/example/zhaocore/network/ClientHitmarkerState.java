package com.example.zhaocore.network;

import com.example.zhaocore.ZhaoCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ZhaoCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientHitmarkerState {

    public static int showTimer = 0;

    public static void showHitmarker() {
        showTimer = 10;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (showTimer > 0) {
                showTimer--;
            }
        }
    }
}