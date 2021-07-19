package net.mcbbs.lh_lshen.chronicler.inscription;

import com.google.common.collect.Lists;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;

import java.util.List;

public class InscriptionRegister {
    public static List<IInscription> inscriptions = Lists.newArrayList();

    public InscriptionRegister() {
        registerInscriptions();
    }

    private void registerInscriptions(){
        register(new InscriptionDysonSphere(EnumInscription.DYSON_SPHERE.getId()));
        register(new InscriptionEternalBlazingSun(EnumInscription.ETERNAL_BLAZING_SUN.getId()));
        register(new InscriptionDarkMatterDrive(EnumInscription.DARK_MATTER_DRIVE.getId()));
        register(new InscriptionHeroCreation(EnumInscription.HERO_CREATION.getId()));
    }
    private void register(IInscription inscription){
        if (!inscriptions.contains(inscription)) {
            inscriptions.add(inscription);
        }
    }
    public static IInscription getInscription(String id){
        for (IInscription inscription : inscriptions){
            if (inscription.getId().equals(id)){
                return inscription;
            }
        }
        return null;
    }
}
