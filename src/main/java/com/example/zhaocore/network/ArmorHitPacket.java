package com.example.zhaocore.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class ArmorHitPacket {

    public ArmorHitPacket() {}

    public ArmorHitPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientHitmarkerState.showHitmarker();
        });
        context.setPacketHandled(true);
        return true;
    }
}