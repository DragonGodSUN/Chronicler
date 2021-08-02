package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.PlayerEntity;
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
            CompoundNBT compoundNBT = (CompoundNBT) message.inscription.serializeNBT();
            buf.writeItemStack(message.stack,false);
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
                synData(message,sender);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    public static void synData(ManageInscriptionCapMessage message, PlayerEntity player){
        if (player == null) {
            return;
        }
        CapabilityInscription cap = (CapabilityInscription) message.inscription;
        ItemStack hold = player.getMainHandItem();
        if (hold.getItem() instanceof ItemChronicler){
            ICapabilityInscription inscription = hold.getCapability(ModCapability.INSCRIPTION_CAPABILITY).orElse(null);
            if (cap!=null && inscription!=null){
                inscription.deserializeNBT(cap.serializeNBT());
                inscription.setDirty(true);
            }
//          按理说cap在之后会将数据再同步到客户端，但不知道为何未能成功，只能手动同步
            NBTHelper.putCapsTag(hold);
        }
    }
}
