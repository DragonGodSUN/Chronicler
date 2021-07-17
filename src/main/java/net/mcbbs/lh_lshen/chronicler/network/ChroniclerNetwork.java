package net.mcbbs.lh_lshen.chronicler.network;

import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.network.packages.*;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.ManageEnergyCapMessage;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.ManageItemListCapMessage;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.SynContainerEnergyCapMessage;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.ManageItemNBTMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
//  这里参考了酒石酸和Boson的代码实例
public class ChroniclerNetwork {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Utils.MOD_ID, "lc_network"),
                () -> VERSION, (version) -> version.equals(VERSION), (version) -> version.equals(VERSION)
        );
        addMessages();
    }

    private static void addMessages() {
        INSTANCE.registerMessage(nextID(), BasicMessage.class, BasicMessage::encode, BasicMessage::decode, BasicMessage::handler,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), ManageItemListCapMessage.class, ManageItemListCapMessage::encode, ManageItemListCapMessage::decode, ManageItemListCapMessage::handler,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), ProduceMessage.class, ProduceMessage::encode, ProduceMessage::decode, ProduceMessage::handler,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), ManageItemNBTMessage.class, ManageItemNBTMessage::encode, ManageItemNBTMessage::decode, ManageItemNBTMessage::handler,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), ManageEnergyCapMessage.class, ManageEnergyCapMessage::encode, ManageEnergyCapMessage::decode, ManageEnergyCapMessage::handler,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(nextID(), SynContainerEnergyCapMessage.class, SynContainerEnergyCapMessage::encode, SynContainerEnergyCapMessage::decode, SynContainerEnergyCapMessage::handler,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
//        INSTANCE.registerMessage(nextID(), SynCapMessage.class, SynCapMessage::encode, SynCapMessage::decode, SynCapMessage::handler,
//                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }


    public static void sendToClientPlayer(Object message, PlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

    public static void sendToNearby(World world, BlockPos pos, Object toSend) {
        if (world instanceof ServerWorld) {
            ServerWorld ws = (ServerWorld) world;

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 192 * 192)
                    .forEach(p -> INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

}
