package net.mcbbs.lh_lshen.chronicler.loot;
//参考了Forge官方文档及Choonster的示例代码TestMod3
//TestMod3： https://github.com/Choonster-Minecraft-Mods/TestMod3
import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.loot.functions.LootNBTRandomFunction;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class LootTableLoader {
    public static final ResourceLocation RECORDER_SUPER = RegistrationHandler.create("chest/recorder_super");
    public static final ResourceLocation INSCRIPTIONS = RegistrationHandler.create("chest/inscriptions");

    public static final LootFunctionType FUNCTION_INSCRIPTION_RANDOM = FunctionHandler.register("set_random_inscription",new LootNBTRandomFunction.Serializer());

    public LootTableLoader() {
        RegistrationHandler.LOOT_TABLES.forEach(LootTables::register);
    }

    public static class RegistrationHandler {

        private static final Set<ResourceLocation> LOOT_TABLES = new HashSet<>();

        protected static ResourceLocation create(final String id) {
            final ResourceLocation lootTable = new ResourceLocation(Utils.MOD_ID, id);
            RegistrationHandler.LOOT_TABLES.add(lootTable);
            return lootTable;
        }
    }

    public static class FunctionHandler{
        private static LootFunctionType register(String type, ILootSerializer<? extends ILootFunction> serializer) {
            return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(type), new LootFunctionType(serializer));
        }
    }
}
