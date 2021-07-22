package net.mcbbs.lh_lshen.chronicler.loot;
//参考了Forge官方文档及Choonster的示例代码TestMod3
//TestMod3： https://github.com/Choonster-Minecraft-Mods/TestMod3
import net.mcbbs.lh_lshen.chronicler.Utils;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LootModifierSerializerRegistry {
    private static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Utils.MOD_ID);

}
