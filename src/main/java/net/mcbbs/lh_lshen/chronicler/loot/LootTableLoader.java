package net.mcbbs.lh_lshen.chronicler.loot;
//参考了Forge官方文档及Choonster的示例代码TestMod3
//TestMod3： https://github.com/Choonster-Minecraft-Mods/TestMod3
import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.loot.functions.LootNBTRandomFunction;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LootTableLoader {
    public static final ResourceLocation CHEST_RECORDER_SUPER = RegistrationHandler.create("chest/recorder_super");
    public static final ResourceLocation CHEST_INSCRIPTIONS = RegistrationHandler.create("chest/inscriptions");
    public static final ResourceLocation CHEST_INSCRIPTIONS_VOID = RegistrationHandler.create("chest/inscriptions_void");

    public static final ResourceLocation ENTITY_RECORDER_LORD = RegistrationHandler.create("entity/recorder_lord");



    public static final LootFunctionType FUNCTION_INSCRIPTION_RANDOM = FunctionHandler.register("set_random_inscription",new LootNBTRandomFunction.Serializer());

    public LootTableLoader() {
        RegistrationHandler.LOOT_TABLES.forEach(LootTables::register);
        for (Map.Entry<ResourceLocation,LootFunctionType> typeEntry : FunctionHandler.FUNCTIONS.entrySet()){
            Registry.register(Registry.LOOT_FUNCTION_TYPE, typeEntry.getKey(),typeEntry.getValue());
        }
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
        public static final Map<ResourceLocation,LootFunctionType> FUNCTIONS = new HashMap<>();

        private static LootFunctionType register(String type, ILootSerializer<? extends ILootFunction> serializer) {
            LootFunctionType functionType = new LootFunctionType(serializer);
            ResourceLocation location = new ResourceLocation(Utils.MOD_ID,type);
            FUNCTIONS.put(location,functionType);
            return functionType;
//            return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(type), new LootFunctionType(serializer));
        }
    }
}
