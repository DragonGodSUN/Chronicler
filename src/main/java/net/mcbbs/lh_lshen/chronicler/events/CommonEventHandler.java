package net.mcbbs.lh_lshen.chronicler.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicier;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {
    public static ContainerType<ContainerChronicier> containerTypeChronicler;

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        containerTypeChronicler = IForgeContainerType.create(ContainerChronicier::createContainerClientSide);
        containerTypeChronicler.setRegistryName("container_chronicler");
        event.getRegistry().register(containerTypeChronicler);
    }

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
//                                    itemTag.putInt("stack_index",entry.getValue().indexOf(itemStack));
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
//                                        int index = itemNbt.getInt("stack_index");
                                        ItemStack stack = ItemStack.of(itemNbt);
                                        id_list.add(id);
                                        stackList_nbt.add(stack);
//                                        stackList_nbt.set(index,stack);
                                    }
                                }
                                if (!stackList_nbt.isEmpty()) {
                                    for (String id : id_list) {
                                        List<ItemStack> stackList = Lists.newArrayList();
                                        for (ItemStack stack : stackList_nbt) {
                                            String stack_id = stack.getItem().getRegistryName().toString();
                                            if (id.equals(stack_id)){
                                                stackList.add(stack);
                                            }
                                        }
                                        itemAllMap_nbt.put(id,stackList);
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
