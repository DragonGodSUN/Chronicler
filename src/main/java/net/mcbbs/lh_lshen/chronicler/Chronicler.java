package net.mcbbs.lh_lshen.chronicler;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

@Mod(Utils.MOD_ID)
public class Chronicler {
    public Chronicler() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EffectRegistry.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
