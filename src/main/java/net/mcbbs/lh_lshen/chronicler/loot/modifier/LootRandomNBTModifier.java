package net.mcbbs.lh_lshen.chronicler.loot.modifier;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;

import java.util.List;


public class LootRandomNBTModifier extends net.minecraftforge.common.loot.LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected LootRandomNBTModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
//        List<ItemStack> newLoot = new ArrayList<>();
//        generatedLoot.forEach((stack) -> {
//            Optional<FurnaceRecipe> optional = context.getLevel().getRecipeManager().getRecipeFor(IRecipeType.SMELTING, new Inventory(stack), context.getLevel());
//            if (optional.isPresent()) {
//                ItemStack smeltedItemStack = optional.get().getRecipeOutput();
//                if (!smeltedItemStack.isEmpty()) {
//                    smeltedItemStack = ItemHandlerHelper.copyStackWithSize(smeltedItemStack, stack.getCount() * smeltedItemStack.getCount());
//                    newLoot.add(smeltedItemStack);
//                }
//                else {
//                    newLoot.add(stack);
//                }
//            }
//            else {
//                newLoot.add(stack);
//            }
//        });
//        return newLoot;
//    }
        return null;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootRandomNBTModifier> {

        @Override
        public LootRandomNBTModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new LootRandomNBTModifier(conditionsIn);
        }

        @Override
        public JsonObject write(LootRandomNBTModifier instance) {
            return null;
        }
    }
}
