package net.mcbbs.lh_lshen.chronicler.helper;

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
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.SynContainerEnergyCapMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.LazyOptional;

public class DataHelper {
//  将服务端的数据发送给客户端的容器
    public static void synEnergyCap(ItemStack itemStack, PlayerEntity player){
        LazyOptional<ICapabilityStellarisEnergy> energyLazyOptional = itemStack.getCapability(ModCapability.ENERGY_CAPABILITY,null);
        energyLazyOptional.ifPresent((energy)->{
            if (energy.isDirty() && player !=null){
                if (player.containerMenu instanceof ContainerChronicler){
                    ICapabilityItemList cap_list = DataHelper.getItemListCapability(itemStack);
                    ChroniclerNetwork.sendToClientPlayer(new SynContainerEnergyCapMessage(cap_list.getUuid(),energy), player);
                }
                energy.setDirty(false);
            }
        });
    }

    public static void synInscriptionPlayerCap(ICapabilityInscription inscription, PlayerEntity player){
        ICapabilityEffectPlayer effectPlayer = getEffectPlayerCapability(player);
        if (inscription!=null){
            effectPlayer.reset();
            effectPlayer.setInscription(inscription.getInscription());
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
}
