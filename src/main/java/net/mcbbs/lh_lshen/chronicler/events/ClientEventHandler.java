package net.mcbbs.lh_lshen.chronicler.events;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.mcbbs.lh_lshen.chronicler.inventory.gui.ChroniclerGUI;
import net.mcbbs.lh_lshen.chronicler.items.ItemRecorder;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(()->{
            ScreenManager.register(CommonEventHandler.containerTypeChronicler, ChroniclerGUI::new);
        });
        event.enqueueWork(ClientEventHandler::registerPropertyOverride);
    }

    public static void registerPropertyOverride() {
        ItemModelsProperties.register(ItemRegistry.recorder.get(), new ResourceLocation("type"), ItemRecorder::getTypePropertyOverride);
    }

}
