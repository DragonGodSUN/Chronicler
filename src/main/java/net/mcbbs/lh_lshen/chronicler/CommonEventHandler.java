package net.mcbbs.lh_lshen.chronicler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
                            for(Map.Entry<String, Map<String,ItemStack>> entry:instance.getAllMap().entrySet()){
                                for (Map.Entry<String, ItemStack> stackEntry:entry.getValue().entrySet()){
                                    CompoundNBT itemTag = new CompoundNBT();
                                    itemTag.putString("item_id",entry.getKey());
                                    itemTag.putString("stack_id",stackEntry.getKey());
                                    stackEntry.getValue().save(itemTag);
                                    nbtTagList.add(itemTag);
                                }
                            }
                            return nbtTagList;
                        }

                        @Override
                        public void readNBT(Capability<ICapabilityItemList> capability, ICapabilityItemList instance, Direction side, INBT nbt) {
                            if(instance instanceof CapabilityItemList && nbt instanceof ListNBT){
                                ListNBT nbtList = (ListNBT) nbt;
                                Map<String, Map<String,ItemStack>> itemAllMap_nbt = Maps.newHashMap();
                                Map<String,ItemStack> stackMap_nbt = Maps.newHashMap();
                                List<String> id_list = Lists.newArrayList();
                                for (INBT tag:nbtList){
                                    if (tag instanceof CompoundNBT){
                                        CompoundNBT itemNbt = (CompoundNBT) tag;
                                        String id = itemNbt.getString("item_id");
                                        String stack_id = itemNbt.getString("stack_id");
                                        ItemStack stack = ItemStack.of(itemNbt);
                                        id_list.add(id);
                                        stackMap_nbt.put(stack_id,stack);
                                    }
                                }
                                for (Map.Entry<String,ItemStack> stackEntry:stackMap_nbt.entrySet()){
                                    String id = stackEntry.getValue().getItem().getRegistryName().toString();
                                    if (id_list.contains(id)){
                                        itemAllMap_nbt.put(id,stackMap_nbt);
                                    }
                                }
                                instance.setAllMap(itemAllMap_nbt);
                            }
                        }
                    },
                    () -> null
            );
            System.out.println("ohhhhhhhhhhhhhhhhhh");
        });
    }
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ChroniclerNetwork::registerMessage);
    }
}
