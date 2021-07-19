package net.mcbbs.lh_lshen.chronicler;

import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.mcbbs.lh_lshen.chronicler.items.ItemInscription;
import net.mcbbs.lh_lshen.chronicler.items.ItemRecordPage;
import net.mcbbs.lh_lshen.chronicler.items.ItemRecorder;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Utils.MOD_ID);
    public static final RegistryObject<Item> chronicle = ITEMS.register("chronicle", ItemChronicler::new);
    public static final RegistryObject<Item> recordPage = ITEMS.register("record_page", ItemRecordPage::new);
    public static final RegistryObject<Item> recorder = ITEMS.register("recorder", ItemRecorder::new);
    public static final RegistryObject<Item> inscription = ITEMS.register("inscription", ItemInscription::new);

}
