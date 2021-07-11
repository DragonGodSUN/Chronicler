package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtomChronicler extends ImageButton {
    private int row;
    private int index;
    public ButtomChronicler(int row, int index, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int textureWidth, int textureHeight, IPressable onPressIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, textureWidth, textureHeight, onPressIn, StringTextComponent.EMPTY);
        this.row = row;
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public int getIndex() {
        return index;
    }

}
