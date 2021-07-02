package net.mcbbs.lh_lshen.chronicler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicle;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onCapabilitySetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CapabilityManager.INSTANCE.register(
                    ICapabilityItemList.class,
                    new Capability.IStorage<ICapabilityItemList>() {
                        @Nullable
                        @Override
                        public INBT writeNBT(Capability<ICapabilityItemList> capability, ICapabilityItemList instance, Direction side) {
                            ListNBT nbtTagList = new ListNBT();
                            for(Map.Entry<String, List<ItemStack>> entry:instance.getAllMap().entrySet()){
                                for (ItemStack itemStack:entry.getValue()){
                                    CompoundNBT itemTag = new CompoundNBT();
                                    itemTag.putString("item_id",entry.getKey());
                                    itemTag.putInt("stack_index",entry.getValue().indexOf(itemStack));
                                    itemStack.save(itemTag);
                                    nbtTagList.add(itemTag);
                                }
                            }
                            return nbtTagList;
                        }

                        @Override
                        public void readNBT(Capability<ICapabilityItemList> capability, ICapabilityItemList instance, Direction side, INBT nbt) {
                            if(instance instanceof CapabilityItemList && nbt instanceof ListNBT){
                                ListNBT nbtList = (ListNBT) nbt;
                                Map<String, List<ItemStack>> itemAllMap_nbt = Maps.newHashMap();
                                List<ItemStack> stackList_nbt = Lists.newArrayList();
                                List<String> id_list = Lists.newArrayList();
                                for (INBT tag:nbtList){
                                    if (tag instanceof CompoundNBT){
                                        CompoundNBT itemNbt = (CompoundNBT) tag;
                                        String id = itemNbt.getString("item_id");
                                        int index = itemNbt.getInt("stack_index");
                                        ItemStack stack = ItemStack.of(itemNbt);
                                        id_list.add(id);
                                        stackList_nbt.set(index,stack);
                                    }
                                }
                                for (ItemStack itemStack:stackList_nbt){
                                    String id = itemStack.getItem().getRegistryName().toString();
                                    if (id_list.contains(id)){
                                        itemAllMap_nbt.put(id,stackList_nbt);
                                    }
                                }
                                instance.setAllMap(itemAllMap_nbt);
                            }
                        }
                    },CapabilityItemList::new);
        });
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ChroniclerNetwork::registerMessage);
    }

//    @SubscribeEvent
//    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
//        ItemStack itemStack = event.getObject();
//        if (itemStack.getItem() instanceof ItemChronicle) {
//            event.addCapability(new ResourceLocation(Utils.MOD_ID, "store"), new ItemListProvider());
//        }
//    }
}
