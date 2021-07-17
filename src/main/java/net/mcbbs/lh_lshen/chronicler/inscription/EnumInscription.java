package net.mcbbs.lh_lshen.chronicler.inscription;

public enum  EnumInscription {
    DYSON_SPHERE("dyson_sphere"),
    ETERNAL_BLAZING_SUN("eternal_blazing_sun"),
    DARK_MATTER_DRIVE("dark_matter_drive"),
    HOPE_FLOWER("hope_flower"),
    HERO_CREATION("hero_creation")
    ;
    private String id;

    EnumInscription(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
