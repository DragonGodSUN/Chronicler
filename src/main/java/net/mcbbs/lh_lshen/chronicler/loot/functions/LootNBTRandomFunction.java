package net.mcbbs.lh_lshen.chronicler.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.mcbbs.lh_lshen.chronicler.inscription.IInscription;
import net.mcbbs.lh_lshen.chronicler.inscription.InscriptionRegister;
import net.mcbbs.lh_lshen.chronicler.items.ItemInscription;
import net.mcbbs.lh_lshen.chronicler.loot.LootTableLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Random;

public class LootNBTRandomFunction extends LootFunction {

    protected LootNBTRandomFunction(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        List<IInscription> inscriptions = InscriptionRegister.inscriptions;
        if (inscriptions.size()>0) {
        int i = MathHelper.nextInt(new Random(),0,inscriptions.size()-1);
            ItemStack new_stack = ItemInscription.getSubStack(inscriptions.get(i).getId());
            return new_stack;
        }
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return LootTableLoader.FUNCTION_INSCRIPTION_RANDOM;
    }


    public static final class Serializer extends LootFunction.Serializer<LootNBTRandomFunction> {

        @Override
        public void serialize(JsonObject jsonObject, LootNBTRandomFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
        }

        @Override
        public LootNBTRandomFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, ILootCondition[] conditions) {
            return new LootNBTRandomFunction(conditions);
        }
    }
}
