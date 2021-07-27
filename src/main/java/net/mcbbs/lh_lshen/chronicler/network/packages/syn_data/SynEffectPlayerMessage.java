package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.UUID;
import java.util.function.Supplier;

public class SynEffectPlayerMessage {
    private String id = UUID.randomUUID().toString();
    private ICapabilityEffectPlayer effectInscription;

    public SynEffectPlayerMessage(String id, ICapabilityEffectPlayer effectInscription) {
        this.id = id;
        this.effectInscription = effectInscription;
    }

    public static void encode(SynEffectPlayerMessage message, PacketBuffer buf) {
        if (message.effectInscription !=null) {
            CompoundNBT compoundNBT = message.effectInscription.serializeNBT();
            buf.writeUtf(message.id);
            buf.writeNbt(compoundNBT);
        }
    }

    public static SynEffectPlayerMessage decode(PacketBuffer buf) {
        String uuid = buf.readUtf();
        CompoundNBT nbt = buf.readNbt();
        CapabilityEffectPlayer effectInscription = new CapabilityEffectPlayer();
        effectInscription.deserializeNBT(nbt);
        if (effectInscription!=null) {
            return new SynEffectPlayerMessage(uuid,effectInscription);
        }
        return null;
    }

    public static void handler(SynEffectPlayerMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                setCapability(message);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void setCapability(SynEffectPlayerMessage message){
        CapabilityEffectPlayer cap = (CapabilityEffectPlayer) message.effectInscription;
        UUID uuid = UUID.fromString(message.id);
        if (Minecraft.getInstance().player!=null) {
            PlayerEntity player = Minecraft.getInstance().player.level.getPlayerByUUID(uuid);
            if (cap != null && player != null) {
                ICapabilityEffectPlayer cap_clent = DataHelper.getEffectPlayerCapability(player);
                cap_clent.deserializeNBT(cap.serializeNBT());
            }
        }
    }
}
