package net.mcbbs.lh_lshen.chronicler.loot;

import net.mcbbs.lh_lshen.chronicler.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Utils.MOD_ID)
public class LootTableModule {
//    public static final ResourceLocation CHEST_VOID = new ResourceLocation("chests/simple_dungeon");
//    public static final ResourceLocation CHEST_VOID = new ResourceLocation(Utils.MOD_ID,"loot_tables/chest/chest_void");

    @SubscribeEvent
    public static void lootTableLoad(LootTableLoadEvent event) {
        if (event.getTable().getLootTableId().equals(LootTables.END_CITY_TREASURE)){
            addPool(LootTableLoader.RECORDER_SUPER,event);
            addPool(LootTableLoader.INSCRIPTIONS,event);
        }
        if (event.getTable().getLootTableId().equals(LootTables.SHIPWRECK_TREASURE)){
            addPool(LootTableLoader.RECORDER_SUPER,event);
            addPool(LootTableLoader.INSCRIPTIONS,event);
        }
        if (event.getTable().getLootTableId().equals(LootTables.NETHER_BRIDGE)){
            addPool(LootTableLoader.RECORDER_SUPER,event);
            addPool(LootTableLoader.INSCRIPTIONS,event);
        }

    }

    public static void addPool(ResourceLocation table , LootTableLoadEvent event) {
        LootPool pool = poolFromFile(table);
        event.getTable().addPool(pool);
    }

    private static LootPool poolFromFile(ResourceLocation table){
        StandaloneLootEntry.Builder<?> entry = TableLootEntry.lootTableReference(table);
        return LootPool.lootPool().add(entry).build();
    }
}
