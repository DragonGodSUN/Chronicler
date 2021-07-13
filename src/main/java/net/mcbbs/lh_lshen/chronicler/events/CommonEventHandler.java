package net.mcbbs.lh_lshen.chronicler.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.util.Pair;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
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
    public static ContainerType<ContainerChronicler> containerTypeChronicler;

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        containerTypeChronicler = IForgeContainerType.create(ContainerChronicler::createContainerClientSide);
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
                            return instance.serializeNBT();
                        }

                        @Override
                        public void readNBT(Capability<ICapabilityItemList> capability, ICapabilityItemList instance, Direction side, INBT nbt) {
                            if(instance instanceof CapabilityItemList && nbt instanceof ListNBT){
                                instance.deserializeNBT((ListNBT) nbt);
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
