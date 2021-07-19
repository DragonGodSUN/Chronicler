package net.mcbbs.lh_lshen.chronicler;

import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Utils.MOD_ID);
    public static final RegistryObject<Effect> hope_flower = EFFECTS.register("hope_flower", EffectHopeFlower::new);

}
