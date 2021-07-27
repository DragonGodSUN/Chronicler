package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.network.packages.BasicMessage;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynItemNBTInHandMessage extends BasicMessage {
    private ItemStack itemStack;
    public SynItemNBTInHandMessage(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static void encode(SynItemNBTInHandMessage message, PacketBuffer buf) {
        buf.writeItemStack(message.itemStack,false);
    }

    public static SynItemNBTInHandMessage decode(PacketBuffer buf) {
        return new SynItemNBTInHandMessage(buf.readItem());
    }

    public static void handler(SynItemNBTInHandMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                ClientPlayerEntity player = (ClientPlayerEntity) DataHelper.getClientPlayer();
                if (player == null) {
                    return;
                }
                ItemStack itemStack = message.itemStack;
                ItemStack hold = player.getMainHandItem();
                if (itemStack != null && hold.getItem().equals(itemStack.getItem())) {
                    hold.setTag(itemStack.getOrCreateTag());
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
