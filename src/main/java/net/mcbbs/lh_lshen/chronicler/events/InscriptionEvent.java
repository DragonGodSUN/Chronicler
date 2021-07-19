package net.mcbbs.lh_lshen.chronicler.events;

import com.google.common.eventbus.Subscribe;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.inscription.EnumInscription;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class InscriptionEvent {

    @SubscribeEvent
    public static void MakeHeroCreationEffect(LivingHurtEvent event){
        if (event.getSource().getEntity() instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity) event.getSource().getEntity();
            ICapabilityEffectPlayer efectInscription = DataHelper.getEffectPlayerCapability(entity);
            if (efectInscription.getInscription().equals(EnumInscription.HERO_CREATION.getId())){
                event.setAmount(event.getAmount()*100);
            }
//            for (int i = 0;i < entity.inventory.getContainerSize();i++){
//                ItemStack itemStack = entity.inventory.getItem(i);
//                if (itemStack.getItem() instanceof ItemChronicler){
//                    ICapabilityInscription inscription = DataHelper.getInscriptionCapability(itemStack);
//                    if (inscription.getInscription().equals(EnumInscription.HERO_CREATION.getId())){
//                        event.setAmount(event.getAmount()*2);
//                    }
//                }
//            }
        }
    }
}
