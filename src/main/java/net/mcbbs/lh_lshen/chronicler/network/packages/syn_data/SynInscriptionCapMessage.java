package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynInscriptionCapMessage {
    private ICapabilityInscription inscription;
    private ItemStack stack;

    public SynInscriptionCapMessage(ItemStack stack, ICapabilityInscription inscription) {
        this.stack = stack;
        this.inscription = inscription;
    }

    public static void encode(SynInscriptionCapMessage message, PacketBuffer buf) {
        if (message.stack !=null && message.inscription != null) {
            CompoundNBT compoundNBT = (CompoundNBT) message.inscription.serializeNBT();
            buf.writeItemStack(message.stack,false);
            buf.writeNbt(compoundNBT);
        }
    }

    public static SynInscriptionCapMessage decode(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        CompoundNBT nbt = buf.readNbt();
        CapabilityInscription inscription = new CapabilityInscription();
        inscription.deserializeNBT(nbt);
        if (inscription!=null) {
            return new SynInscriptionCapMessage(stack,inscription);
        }
        return null;
    }

    public static void handler(SynInscriptionCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                synData(message);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    public static void synData(SynInscriptionCapMessage message){
        PlayerEntity player = DataHelper.getClientPlayer();
        if (player == null) {
            return;
        }
        CapabilityInscription cap = (CapabilityInscription) message.inscription;
        ItemStack hold = player.getMainHandItem();
        if (hold.getItem() instanceof ItemChronicler
            && hold.equals(message.stack,true)){
            ICapabilityInscription inscription = hold.getCapability(ModCapability.INSCRIPTION_CAPABILITY).orElse(null);
            if (cap!=null && inscription!=null){
                inscription.deserializeNBT(cap.serializeNBT());
            }
        }
    }
}
