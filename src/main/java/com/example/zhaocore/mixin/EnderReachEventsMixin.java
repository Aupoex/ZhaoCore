package com.example.zhaocore.mixin;

import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SpecialEquipmentEvents.class, remap = false)
public class EnderReachEventsMixin {

    @Redirect(
            method = {"onEntityDrop", "onEntityJoinLevel"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;)I")
    )
    private static int redirectEnderEnchantmentLevel(ItemStack instance, Enchantment enchantment) {
        int originalLevel = instance.getEnchantmentLevel(enchantment);

        if (enchantment == LCEnchantments.ENDER.get() && !instance.isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (player.getMainHandItem() == instance) {

                        double attrValue = player.getAttributeValue(com.example.zhaocore.ModRegistry.ENDER_REACH.get());
                        int attributeLevel = (int) attrValue;

                        return Math.max(originalLevel, attributeLevel);
                    }
                }
            }
        }
        return originalLevel;
    }
}