package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicier;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class ChroniclerGUI extends ContainerScreen<ContainerChronicier> {
    TextFieldWidget textFieldWidget;
    Button button;
    ResourceLocation TEXTURE = new ResourceLocation(Utils.MOD_ID, "textures/gui/door.png");
    TranslationTextComponent content = new TranslationTextComponent("gui." + Utils.MOD_ID + ".first");
    SliderPercentageOption sliderPercentageOption;
    Widget sliderBar;


    public ChroniclerGUI(ContainerChronicier container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        
    }



//    @Override
//    protected void init() {
//        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
//        this.textFieldWidget = new TextFieldWidget(this.font, this.width / 2 - 100, 66, 200, 20, new TranslationTextComponent("gui." + Utils.MOD_ID + ".first.context"));
//        this.children.add(this.textFieldWidget);
//
//        this.button = new Button(this.width / 2 - 40, 96, 80, 20, new TranslationTextComponent("gui." + Utils.MOD_ID + ".first.save"), (button) -> {
//        });
////        this.addButton(button);
//
//        this.sliderPercentageOption = new SliderPercentageOption(Utils.MOD_ID + ".sliderbar", 5, 100, 5, (setting) -> Double.valueOf(0), (setting, value) -> {
//        }, (gameSettings, sliderPercentageOption1) -> new TranslationTextComponent("gui." + Utils.MOD_ID + ".first.test"));
//        this.sliderBar = this.sliderPercentageOption.createButton(Minecraft.getInstance().options, this.width / 2 - 100, 120, 200);
//        this.children.add(this.sliderBar);
//
//        super.init();
//    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//        this.renderBackground(matrixStack);
//        this.setFocused((IGuiEventListener)null);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.minecraft.getTextureManager().bind(TEXTURE);
//        int textureWidth = 256;
//        int textureHeight = 184;
//        matrixStack.pushPose();
//        matrixStack.scale(1.5f,1.5f,1.5f);
//        this.blit(matrixStack, (int) ((this.width/2/1.5 - 128)), (int) (20/1.5f), 0, 0, 256, 184, textureWidth, textureHeight);
//        matrixStack.popPose();
//        drawCenteredString(matrixStack, this.font, content, this.width / 2 - 10, 30, 0xeb0505);
//        this.textFieldWidget.render(matrixStack, mouseX, mouseY, partialTicks);
//        this.button.render(matrixStack, mouseX, mouseY, partialTicks);
//        this.sliderBar.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderComponentHoverEffect(matrixStack,null, mouseX, mouseY);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int p_238651_2_) {
        super.renderBackground(matrixStack, p_238651_2_);
    }

    @Override
    protected void renderComponentHoverEffect(MatrixStack p_238653_1_, @Nullable Style p_238653_2_, int p_238653_3_, int p_238653_4_) {
        super.renderComponentHoverEffect(p_238653_1_, p_238653_2_, p_238653_3_, p_238653_4_);
    }

    @Override
    public void renderComponentTooltip(MatrixStack p_243308_1_, List<ITextComponent> p_243308_2_, int p_243308_3_, int p_243308_4_) {
        super.renderComponentTooltip(p_243308_1_, p_243308_2_, p_243308_3_, p_243308_4_);
    }

    @Override
    public void renderDirtBackground(int p_231165_1_) {
        super.renderDirtBackground(p_231165_1_);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int textureWidth = 256;
        int textureHeight = 184;
        matrixStack.pushPose();
        matrixStack.scale(1.5f,1.5f,1.5f);
        this.blit(matrixStack, (int) ((this.width/2/1.5 - 128)), (int) (20/1.5f), 0, 0, 256, 184, textureWidth, textureHeight);
        matrixStack.popPose();
    }
}
