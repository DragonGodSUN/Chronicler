package net.mcbbs.lh_lshen.chronicler.helper;

import net.mcbbs.lh_lshen.chronicler.Config;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

import java.util.UUID;

/**
 * 操作能力数据的工具类
 */
public class DataHelper {
//  将服务端的数据发送给客户端的容器
public static void synEnergyContanierCap(ItemStack itemStack, PlayerEntity player){
    LazyOptional<ICapabilityStellarisEnergy> energyLazyOptional = itemStack.getCapability(ModCapability.ENERGY_CAPABILITY,null);
    energyLazyOptional.ifPresent((energy)->{
        if (energy.isDirty() && player !=null){
            if (player.containerMenu instanceof ContainerChronicler){
                ChroniclerNetwork.sendToClientPlayer(new SynContainerEnergyCapMessage(ItemChronicler.getId(itemStack),energy), player);
            }
            energy.setDirty(false);
        }
    });
}

    public static void capsUpdae(ItemStack itemStack){
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemChronicler){
            ICapabilityItemList cap_list = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);;
            ICapabilityStellarisEnergy energy = itemStack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
            ICapabilityInscription inscription = itemStack.getCapability(ModCapability.INSCRIPTION_CAPABILITY).orElse(null);
//          强制客户端和服务端加载能力值
            if (cap_list!=null) {
                cap_list.loadIfNotLoaded(itemStack);
            }
            if (energy!=null) {
                energy.loadIfNotLoaded(itemStack);
            }
            if (inscription!=null) {
                inscription.loadIfNotLoaded(itemStack);
            }
        }
    }
    public static void synChroniclerCaps(ItemStack itemStack, PlayerEntity player){
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemChronicler){
            ICapabilityItemList cap_list = getItemListCapability(itemStack);
            ICapabilityStellarisEnergy energy = getStellarisEnergyCapability(itemStack);
            ICapabilityInscription inscription = getInscriptionCapability(itemStack);
//          防止客户端误操作
            if (!player.level.isClientSide()) {
//          同步储存物品与刻印数据，但其实没什么用，主要在GUI操作的过程中同步
                if (cap_list.isDirty() || inscription.isDirty()) {
                    NBTHelper.putCapsTag(itemStack);
                    if (player.getMainHandItem().equals(itemStack)){
                        ChroniclerNetwork.sendToClientPlayer(new SynItemNBTInHandMessage(itemStack), player);
                    }else {
                        ChroniclerNetwork.sendToClientPlayer(new SynChroniclerMessage(itemStack),player);
                    }
                  cap_list.setDirty(false);
                  inscription.setDirty(false);
                }
                if (energy.isDirty()){
//              由于是发送整个tag数据，为了减少服务器压力，默认情况下不同步
                    if (Config.SYN_ENERGY.get()) {
//              压缩能力数据到tag中
                        NBTHelper.putCapsTag(itemStack);
//              同步物品在客户端的数据
                        if (player.getMainHandItem().equals(itemStack)){
                            ChroniclerNetwork.sendToClientPlayer(new SynItemNBTInHandMessage(itemStack), player);
                        }else {
                            ChroniclerNetwork.sendToClientPlayer(new SynChroniclerMessage(itemStack),player);
                        }
                    }
//              这里仅发送能量相关数据，体量较小，可实时同步
                    if (player.containerMenu instanceof ContainerChronicler) {
                        ChroniclerNetwork.sendToClientPlayer(new SynContainerEnergyCapMessage(ItemChronicler.getId(itemStack),energy), player);
                    }
                    energy.setDirty(false);
                }
            }
        }
    }

    public static void synInscriptionPlayerCap(ICapabilityInscription inscription, PlayerEntity player){
        ICapabilityEffectPlayer effectPlayer = getEffectPlayerCapability(player);
        if (inscription!=null){
            effectPlayer.reset();
            effectPlayer.setType(inscription.getInscription());
            effectPlayer.setLevel(inscription.getLevel());
        }
    }

    public static CapabilityItemList getItemListCapability(ItemStack itemStack){
        ICapabilityItemList cap_list = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);
        if (cap_list == null || !(cap_list instanceof ICapabilityItemList)) {
            return new CapabilityItemList();
        }
        return (CapabilityItemList) cap_list;
    }

    public static CapabilityStellarisEnergy getStellarisEnergyCapability(ItemStack itemStack){
        ICapabilityStellarisEnergy energy = itemStack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
        if (energy == null || !(energy instanceof ICapabilityStellarisEnergy)) {
            return new CapabilityStellarisEnergy();
        }
        return (CapabilityStellarisEnergy) energy;
    }

    public static CapabilityInscription getInscriptionCapability(ItemStack itemStack){
        ICapabilityInscription inscription = itemStack.getCapability(ModCapability.INSCRIPTION_CAPABILITY).orElse(null);
        if (inscription == null || !(inscription instanceof ICapabilityInscription)) {
            return new CapabilityInscription();
        }
        return (CapabilityInscription) inscription;
    }

    public static CapabilityEffectPlayer getEffectPlayerCapability(Entity entity){
        ICapabilityEffectPlayer effectPlayer = entity.getCapability(ModCapability.EFFECT_PLAYER).orElse(null);
        if (effectPlayer == null || !(effectPlayer instanceof ICapabilityEffectPlayer)) {
            return new CapabilityEffectPlayer();
        }
        return (CapabilityEffectPlayer) effectPlayer;
    }


    public static ItemStack getItemStackByID(String id, PlayerInventory inventory){
        ItemStack target = ItemStack.EMPTY;
        for (int i=0;i<inventory.getContainerSize();i++){
            ItemStack item = inventory.getItem(i);
            if (item.getItem() instanceof ItemChronicler){
                String id_i = ItemChronicler.getId(item);
                if (id_i.equals(id)){
                    return item;
                }
            }
        }
        return target;
    }

    @OnlyIn(Dist.CLIENT)
    public static PlayerEntity getClientPlayerNearby(String uuid){
        if (Minecraft.getInstance().player != null) {
            UUID u = UUID.fromString(uuid);
            return Minecraft.getInstance().player.level.getPlayerByUUID(u);
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public static PlayerEntity getClientPlayer(){
        if (Minecraft.getInstance().player != null) {
            return Minecraft.getInstance().player;
        }
        return null;
    }

}
