package net.mcbbs.lh_lshen.chronicler.network;

import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.network.packages.BasicPack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        addMessages();
    }

    private static void addMessages() {
        INSTANCE.messageBuilder(BasicPack.class, nextID())
                .encoder(BasicPack::toBytes)
                .decoder(BasicPack::new)
                .consumer(BasicPack::handler)
                .add();
    }

}
