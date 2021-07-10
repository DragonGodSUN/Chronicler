package net.mcbbs.lh_lshen.chronicler.network.packages;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BasicMessage {
    public BasicMessage() {
    }

    public static void encode(BasicMessage message, PacketBuffer buf) {

    }

    public static BasicMessage decode(PacketBuffer buf) {
        return new BasicMessage();
    }

    public static void handler(BasicMessage pack, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
        });
    }
}
