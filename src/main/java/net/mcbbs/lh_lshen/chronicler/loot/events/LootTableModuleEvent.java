package net.mcbbs.lh_lshen.chronicler.loot.events;

import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.loot.LootTableLoader;
import net.minecraft.data.LootTableProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.List;
import java.util.Random;

//感谢@LocusAzzurro提供的关于loottable生成pool的技术指导
//让自定义战利品表在想要的地方生成
@Mod.EventBusSubscriber(modid = Utils.MOD_ID)
public class LootTableModuleEvent {
    @SubscribeEvent
    public static void lootTableLoad(LootTableLoadEvent event) {
        if (event.getTable().getLootTableId().equals(LootTables.END_CITY_TREASURE)){
            addPool(LootTableLoader.CHEST_RECORDER_SUPER,event);
            addPool(LootTableLoader.CHEST_INSCRIPTIONS_VOID,event);
        }
        if (event.getTable().getLootTableId().equals(LootTables.SHIPWRECK_TREASURE)){
            addPool(LootTableLoader.CHEST_RECORDER_SUPER,event);
            addPool(LootTableLoader.CHEST_INSCRIPTIONS_VOID,event);
        }
        if (event.getTable().getLootTableId().equals(LootTables.NETHER_BRIDGE)){
            addPool(LootTableLoader.CHEST_RECORDER_SUPER,event);
            addPool(LootTableLoader.CHEST_INSCRIPTIONS,event);
        }
        if (event.getTable().getLootTableId().equals(LootTables.DESERT_PYRAMID)){
            addPool(LootTableLoader.CHEST_RECORDER_SUPER,event);
            addPool(LootTableLoader.CHEST_INSCRIPTIONS,event);
        }
        if (event.getTable().getLootTableId().equals(LootTables.BASTION_TREASURE)){
            addPool(LootTableLoader.CHEST_RECORDER_SUPER,event);
            addPool(LootTableLoader.CHEST_INSCRIPTIONS,event);
        }

    }

    @SubscribeEvent
    public static void lootTableBossDeath(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getMaxHealth()>175 && entity instanceof MobEntity){
            if (entity.level instanceof ServerWorld) {
                Collection<ItemEntity> collection = event.getDrops();
                LootTable lootTable = LootTable.lootTable().build();
                LootContext context = new LootContext.Builder((ServerWorld) entity.level)
                        .withRandom(new Random()).create(new LootParameterSet.Builder().build());
                lootTable.addPool(poolFromFile(LootTableLoader.ENTITY_RECORDER_LORD));
                List<ItemStack> stackList = lootTable.getRandomItems(context);
                for (ItemStack stack : stackList){
                    collection.add(new ItemEntity(entity.level,
                            entity.position().x,entity.position().y,entity.position().z,
                            stack));
                }
            }
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
