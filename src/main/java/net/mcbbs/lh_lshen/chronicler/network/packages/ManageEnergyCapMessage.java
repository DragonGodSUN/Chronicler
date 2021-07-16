package net.mcbbs.lh_lshen.chronicler.network.packages;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManageEnergyCapMessage {
    private ICapabilityStellarisEnergy energy;
    private ItemStack stack;

    public ManageEnergyCapMessage(ItemStack stack, ICapabilityStellarisEnergy energy) {
        this.stack = stack;
        this.energy = energy;
    }

    public static void encode(ManageEnergyCapMessage message, PacketBuffer buf) {
        if (message.stack !=null && message.energy != null) {
            CompoundNBT compoundNBT = message.energy.serializeNBT();
            buf.writeItemStack(message.stack,true);
            buf.writeNbt(compoundNBT);
        }
    }

    public static ManageEnergyCapMessage decode(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        CompoundNBT nbt = buf.readNbt();
        CapabilityStellarisEnergy energy = new CapabilityStellarisEnergy();
        energy.deserializeNBT(nbt);
        if (energy!=null) {
            return new ManageEnergyCapMessage(stack,energy);
        }
        return null;
    }

    public static void handler(ManageEnergyCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                CapabilityStellarisEnergy cap = (CapabilityStellarisEnergy) message.energy;
                if (sender.inventory.contains(message.stack)){
                    ICapabilityStellarisEnergy energy = message.stack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
                    if (cap!=null && energy!=null){
                        energy.deserializeNBT(cap.serializeNBT());
                    }
                }

            });
            ctx.get().setPacketHandled(true);
        }
    }
}
