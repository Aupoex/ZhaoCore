package com.example.zhaocore.mixin;

import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Mixin(EntityUtil.class)
public abstract class EntityUtilMixin {

    @Redirect(
            method = {
                    "findEntityOnPath",
                    "findEntitiesOnPath"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private static List<Entity> taczignorepets$ignoreOwnedEntities(
            Level level,
            Entity except,
            AABB boundingBox,
            Predicate<? super Entity> originalPredicate
    ) {
        if (!(except instanceof EntityKineticBullet bullet)) {
            return level.getEntities(except, boundingBox, originalPredicate);
        }

        Entity shooter = bullet.getOwner();

        if (!(shooter instanceof Player player)) {
            return level.getEntities(except, boundingBox, originalPredicate);
        }

        UUID playerUuid = player.getUUID();

        Predicate<Entity> newPredicate = entity -> {
            if (!originalPredicate.test(entity)) {
                return false;
            }

            if (entity instanceof OwnableEntity ownableEntity) {
                UUID ownerUuid = ownableEntity.getOwnerUUID();

                if (ownerUuid != null && ownerUuid.equals(playerUuid)) {
                    return false;
                }
            }

            return true;
        };

        return level.getEntities(except, boundingBox, newPredicate);
    }
}