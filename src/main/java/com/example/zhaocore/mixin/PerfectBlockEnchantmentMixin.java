package com.example.zhaocore.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "mod.chloeprime.apotheosismodernragnarok.common.enchantment.PerfectBlockEnchantment", remap = false)
public class PerfectBlockEnchantmentMixin {

    @Redirect(
            method = "onGunMelee",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;)I")
    )
    private int redirectPerfectBlockLevel(ItemStack instance, Enchantment enchantment) {
        int originalLevel = instance.getEnchantmentLevel(enchantment);

        if (!instance.isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {

                    if (player.getMainHandItem() == instance || player.getOffhandItem() == instance) {

                        double attrValue = player.getAttributeValue(com.example.zhaocore.ModRegistry.PERFECT_BLOCK.get());
                        int attributeLevel = (int) attrValue;


                        return Math.max(originalLevel, attributeLevel);
                    }
                }
            }
        }
        return originalLevel;
    }
}