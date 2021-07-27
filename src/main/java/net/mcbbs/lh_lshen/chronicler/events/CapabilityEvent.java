package net.mcbbs.lh_lshen.chronicler.events;

import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.EffectPlayerProvider;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.SynEffectPlayerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public final class CapabilityEvent {
//    private static final ResourceLocation ITEM_LIST = new ResourceLocation(Utils.MOD_ID, "item_list");
//    private static final ResourceLocation ENERGY = new ResourceLocation(Utils.MOD_ID, "stellaris_energy");
//    private static final ResourceLocation INSCRIPTION = new ResourceLocation(Utils.MOD_ID, "inscription");

//    @SubscribeEvent
//    public static void onAttachItemStackCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
//        ItemStack itemStack = event.getObject();
//        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemChronicler) {
//            if (!itemStack.getOrCreateTag().getBoolean("hasCap")) {
//                event.addCapability(ITEM_LIST, new ItemListProvider());
//                event.addCapability(ENERGY, new StellarisEnergyProvider());
//                event.addCapability(INSCRIPTION, new InscriptionProvider());
//                itemStack.getOrCreateTag().putBoolean("hasCap",true);
//            }
//        }
//    }

    @SubscribeEvent
    public static void onAttachEntityCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Utils.MOD_ID, "inscription_effect"), new EffectPlayerProvider());
        }
    }
//
    @SubscribeEvent
    public static void onItemUptateEvent(TickEvent.PlayerTickEvent event) {
//        PlayerEntity player = event.player;
//        if (!player.level.isClientSide()) {
//            for (int i=0;i<player.inventory.getContainerSize();i++) {
//                ItemStack stack = player.inventory.getItem(i);
//                if (stack.getItem() instanceof ItemChronicler && ItemChronicler.isOpen(stack,player)){
//                    DataHelper.synChroniclerCaps(stack, player);
//                }
//            }
//        }
    }


    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            LazyOptional<ICapabilityEffectPlayer> oldSpeedCap = event.getOriginal().getCapability(ModCapability.EFFECT_PLAYER);
            LazyOptional<ICapabilityEffectPlayer> newSpeedCap = event.getPlayer().getCapability(ModCapability.EFFECT_PLAYER);
            if (oldSpeedCap.isPresent() && newSpeedCap.isPresent()) {
                newSpeedCap.ifPresent((newCap) -> {
                    oldSpeedCap.ifPresent((oldCap) -> {
                        newCap.deserializeNBT(oldCap.serializeNBT());
                    });
                });
            }
        }
    }

    @SubscribeEvent
    public static void synCapEvent(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        ICapabilityEffectPlayer effectInscription = DataHelper.getEffectPlayerCapability(player);
        if (effectInscription!=null && player!=null){
            if (effectInscription.isDirty()){
                ChroniclerNetwork.sendToNearby(player.level,new BlockPos(player.position().x,player.position().y,player.position().z),new SynEffectPlayerMessage(player.getStringUUID(),effectInscription));
                effectInscription.setDirty(false);
            }
        }
    }


}
