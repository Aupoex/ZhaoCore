package com.example.zhaocore;

import net.minecraftforge.common.ForgeConfigSpec;

public class HitSoundConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.ConfigValue<String> SOUND_TIER_1;
    public static final ForgeConfigSpec.ConfigValue<String> SOUND_TIER_2;
    public static final ForgeConfigSpec.ConfigValue<String> SOUND_TIER_3;
    public static final ForgeConfigSpec.ConfigValue<String> SOUND_TIER_4;
    public static final ForgeConfigSpec.ConfigValue<String> SOUND_TIER_5;
    public static final ForgeConfigSpec.ConfigValue<String> SOUND_TIER_6;

    public static final ForgeConfigSpec.IntValue X_OFFSET;
    public static final ForgeConfigSpec.IntValue Y_OFFSET;

    public static final ForgeConfigSpec.DoubleValue NORMAL_SPEED;
    public static final ForgeConfigSpec.DoubleValue SWOOP_SPEED;
    public static final ForgeConfigSpec.DoubleValue TURN_SPEED;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("Hit_Sounds_TACZ");
        BUILDER.comment("音效的ResourceLocation");

        SOUND_TIER_1 = BUILDER.comment("Tier 1: 0% <= Bullet Resistance < 20%")
                .define("sound_tier_1", "minecraft:entity.arrow.hit_player");

        SOUND_TIER_2 = BUILDER.comment("Tier 2: 20% <= Bullet Resistance < 40%")
                .define("sound_tier_2", "minecraft:item.shield.block");

        SOUND_TIER_3 = BUILDER.comment("Tier 3: 40% <= Bullet Resistance < 60%")
                .define("sound_tier_3", "minecraft:block.iron_door.open");

        SOUND_TIER_4 = BUILDER.comment("Tier 4: 60% <= Bullet Resistance < 80%")
                .define("sound_tier_4", "minecraft:block.anvil.land");

        SOUND_TIER_5 = BUILDER.comment("Tier 5: 80% <= Bullet Resistance <= 99%")
                .define("sound_tier_5", "minecraft:entity.zombie.attack_iron_door");

        SOUND_TIER_6 = BUILDER.comment("Tier 6: 99% <= Bullet Resistance <= 100%")
                .define("sound_tier_6", "minecraft:entity.zombie.attack_iron_door");

        X_OFFSET = BUILDER.comment("X Offset")
                .defineInRange("x_offset", 0, -1000, 1000);

        Y_OFFSET = BUILDER.comment("Y Offset")
                .defineInRange("y_offset", 0, -1000, 1000);

        BUILDER.pop();

        BUILDER.push("Phantom_Movement_Settings");

        NORMAL_SPEED = BUILDER.comment("平时盘旋速度 (原版为 0.2)")
                .defineInRange("normal_speed", 0.2D, 0.0D, 10.0D);

        SWOOP_SPEED = BUILDER.comment("俯冲最大速度 (原版为 1.8)")
                .defineInRange("swoop_speed", 1.8D, 0.0D, 20.0D);

        TURN_SPEED = BUILDER.comment("每 Tick 最大转向角度 (原版为 4.0)")
                .defineInRange("turn_speed", 4.0D, 0.0D, 180.0D);

        BUILDER.pop();

        COMMON_CONFIG = BUILDER.build();
    }
}