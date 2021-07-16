package net.mcbbs.lh_lshen.chronicler.network.packages;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynContainerEnergyCapMessage {
    private ICapabilityStellarisEnergy energy;

    public SynContainerEnergyCapMessage(ICapabilityStellarisEnergy energy) {
        this.energy = energy;
    }

    public static void encode(SynContainerEnergyCapMessage message, PacketBuffer buf) {
        if (message.energy!=null) {
            CompoundNBT compoundNBT = message.energy.serializeNBT();
            buf.writeNbt(compoundNBT);
        }
    }

    public static SynContainerEnergyCapMessage decode(PacketBuffer buf) {
        CompoundNBT nbt = buf.readNbt();
        CapabilityStellarisEnergy energy = new CapabilityStellarisEnergy();
        energy.deserializeNBT(nbt);
        if (energy!=null) {
            return new SynContainerEnergyCapMessage(energy);
        }
        return null;
    }

    public static void handler(SynContainerEnergyCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                CapabilityStellarisEnergy cap = (CapabilityStellarisEnergy) message.energy;
                PlayerEntity player = Minecraft.getInstance().player;
                Container container = player.containerMenu;
                if (container instanceof ContainerChronicler){
                    ContainerChronicler containerChronicler = (ContainerChronicler) container;
                    boolean f = containerChronicler.getEnergy().getId().equals(cap.getId());
                    if (f) {
                        containerChronicler.setEnergy(cap);
                    }
                }

            });
            ctx.get().setPacketHandled(true);
        }
    }
}
