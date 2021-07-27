package net.mcbbs.lh_lshen.chronicler.events;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.*;
import net.mcbbs.lh_lshen.chronicler.inscription.InscriptionRegister;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.mcbbs.lh_lshen.chronicler.loot.LootTableLoader;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {
    public static ContainerType<ContainerChronicler> containerTypeChronicler;

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ChroniclerNetwork::registerMessage);
        event.enqueueWork(()->{
            new InscriptionRegister();
            new LootTableLoader();
        });
        event.enqueueWork(() -> {
            CapabilityManager.INSTANCE.register(ICapabilityItemList.class, new ItemChroniclerProvider.ItemListStorage(), CapabilityItemList::new);
            CapabilityManager.INSTANCE.register(ICapabilityStellarisEnergy.class, new ItemChroniclerProvider.EnergyStorage(), CapabilityStellarisEnergy::new);
            CapabilityManager.INSTANCE.register(ICapabilityInscription.class, new ItemChroniclerProvider.InscriptionStorage(), CapabilityInscription::new);
            CapabilityManager.INSTANCE.register(ICapabilityEffectPlayer.class, new EffectPlayerProvider.Storage(), CapabilityEffectPlayer::new);
        });
    }


    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        containerTypeChronicler = IForgeContainerType.create(ContainerChronicler::createContainerClientSide);
        containerTypeChronicler.setRegistryName("container_chronicler");
        event.getRegistry().register(containerTypeChronicler);
    }


}
