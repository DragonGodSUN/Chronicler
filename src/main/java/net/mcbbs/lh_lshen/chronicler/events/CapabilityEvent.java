package net.mcbbs.lh_lshen.chronicler.events;

import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.StellarisEnergyProvider;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public final class CapabilityEvent {

    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack itemStack = event.getObject();
        if (itemStack.getItem() instanceof ItemChronicler) {
            if (ModCapability.ITEMLIST_CAPABILITY!=null) {
                event.addCapability(new ResourceLocation(Utils.MOD_ID, "item_list"), new ItemListProvider());
            }
            if (ModCapability.ENERGY_CAPABILITY!=null) {
                event.addCapability(new ResourceLocation(Utils.MOD_ID, "stellaris_energy"), new StellarisEnergyProvider());
            }
        }
    }
//
//    public static void synCapEvent(TickEvent.PlayerTickEvent event){
//        PlayerEntity player = event.player;
//
//    }



}
