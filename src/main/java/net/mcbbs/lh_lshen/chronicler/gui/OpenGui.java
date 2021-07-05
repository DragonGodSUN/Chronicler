package net.mcbbs.lh_lshen.chronicler.gui;

import net.mcbbs.lh_lshen.chronicler.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;

public class OpenGui {
    public OpenGui() {
        Minecraft.getInstance().setScreen(new ChroniclerGUI(new TranslationTextComponent(Utils.MOD_ID + ".test")));
    }
}
