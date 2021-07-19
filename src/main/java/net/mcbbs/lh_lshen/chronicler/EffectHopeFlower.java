package net.mcbbs.lh_lshen.chronicler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

public class EffectHopeFlower extends Effect {
    private static final ResourceLocation EFFECTS = new ResourceLocation(Utils.MOD_ID, "textures/gui/effects.png");
    protected EffectHopeFlower() {
        super(EffectType.NEUTRAL, 16720896);
    }

//    @Override
//    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
//
//        this.renderHUDEffect(x, y+5, effect, mc, 1F);
//    }

    @Override
    public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, MatrixStack mStack, int x, int y, float z) {
        this.renderHUDEffect(effect,gui,mStack,x,y,z,1.0f);
    }

    @Override
    public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {
        Minecraft.getInstance().textureManager.bind(EFFECTS);
        super.renderHUDEffect(effect,gui,mStack,x,y,z,alpha);
//        gui.blit(mStack,x,y,0,0,18,18);
//        RenderSystem.enableBlend();
//        RenderSystem.blendColor(1.0f,1.0f,1.0f,alpha);
//        RenderSystem.disableBlend();

    }
}
