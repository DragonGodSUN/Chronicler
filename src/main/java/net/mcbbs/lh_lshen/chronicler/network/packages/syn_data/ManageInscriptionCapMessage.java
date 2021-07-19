package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManageInscriptionCapMessage {
    private ICapabilityInscription inscription;
    private ItemStack stack;

    public ManageInscriptionCapMessage(ItemStack stack, ICapabilityInscription inscription) {
        this.stack = stack;
        this.inscription = inscription;
    }

    public static void encode(ManageInscriptionCapMessage message, PacketBuffer buf) {
        if (message.stack !=null && message.inscription != null) {
            CompoundNBT compoundNBT = message.inscription.serializeNBT();
            buf.writeItemStack(message.stack,true);
            buf.writeNbt(compoundNBT);
        }
    }

    public static ManageInscriptionCapMessage decode(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        CompoundNBT nbt = buf.readNbt();
        CapabilityInscription inscription = new CapabilityInscription();
        inscription.deserializeNBT(nbt);
        if (inscription!=null) {
            return new ManageInscriptionCapMessage(stack,inscription);
        }
        return null;
    }

    public static void handler(ManageInscriptionCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                CapabilityInscription cap = (CapabilityInscription) message.inscription;
                ItemStack hold = sender.getMainHandItem();
                if (hold.getItem() instanceof ItemChronicler && hold.equals(message.stack,true)){
                    ICapabilityInscription inscription = hold.getCapability(ModCapability.INSCRIPTION_CAPABILITY).orElse(null);
                    if (cap!=null && inscription!=null){
                        inscription.deserializeNBT(cap.serializeNBT());
                    }
                }

            });
        }
    }
}
