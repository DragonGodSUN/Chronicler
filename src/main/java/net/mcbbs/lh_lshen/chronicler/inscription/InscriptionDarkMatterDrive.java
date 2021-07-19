package net.mcbbs.lh_lshen.chronicler.inscription;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class InscriptionDarkMatterDrive implements IInscription {
    private String id;

    public InscriptionDarkMatterDrive(String id) {
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
            int max = 10000+inscription.getLevel()*10000;
            if (energy.getEnergyMax()!=max ) {
                energy.setEnergyMax(max);
            }
        }
    }

    @Override
    public void process(ItemStack itemStack) {

    }
}
