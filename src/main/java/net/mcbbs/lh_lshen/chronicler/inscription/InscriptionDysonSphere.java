package net.mcbbs.lh_lshen.chronicler.inscription;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.logging.Level;

public class InscriptionDysonSphere implements IInscription {
    private String id;

    public InscriptionDysonSphere(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void tickEffect(Entity entity, ItemStack chronicler) {
        ICapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(chronicler);
        ICapabilityInscription inscription = DataHelper.getInscriptionCapability(chronicler);
        if (energy!=null){
            energy.charge(10 * inscription.getLevel());
        }
    }

    @Override
    public void process(ItemStack itemStack) {

    }
}
