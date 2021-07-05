package net.mcbbs.lh_lshen.chronicler;

import net.mcbbs.lh_lshen.chronicler.items.ItemChronicle;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Utils.MOD_ID);
    public static final RegistryObject<Item> chronicle = ITEMS.register("chronicle", ItemChronicle::new);

}
