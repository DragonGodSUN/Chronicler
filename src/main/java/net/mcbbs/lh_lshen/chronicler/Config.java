package net.mcbbs.lh_lshen.chronicler;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.IntValue STORE_MAX;
    public static ForgeConfigSpec.IntValue NBT_MAX;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        writeConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void writeConfig(ForgeConfigSpec.Builder COMMON_BUILDER){
        COMMON_BUILDER.comment("General Settings").push("general");
        STORE_MAX = COMMON_BUILDER.comment("Stored Items MAX").defineInRange("store_max", 100, 0, Integer.MAX_VALUE);
        NBT_MAX = COMMON_BUILDER.comment("Stored Tag NBT MAX").defineInRange("nbt_max", 1024, 0, Integer.MAX_VALUE);
    }
}
