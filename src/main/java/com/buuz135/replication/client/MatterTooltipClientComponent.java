package com.buuz135.replication.client;

import com.buuz135.replication.Replication;
import com.buuz135.replication.aequivaleo.ReplicationCompoundType;
import com.ldtteam.aequivaleo.api.compound.CompoundInstance;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.awt.*;

public class MatterTooltipClientComponent implements ClientTooltipComponent {

    public static final ResourceLocation BAR = new ResourceLocation(Replication.MOD_ID, "textures/gui/matter_bar.png");
    private final MatterTooltipComponent instance;

    public MatterTooltipClientComponent(MatterTooltipComponent instance) {
        this.instance = instance;
    }

    @Override
    public int getHeight() {
        return 24;
    }

    @Override
    public int getWidth(Font p_169952_) {
        return 18 * instance.getInstance().length;
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        //var text = String.valueOf(instance.getInstance().getAmount());
        //var length = 12;
        //font.drawInBatch(text, (float) ((float)x + 52 - font.width(text) / 2D + length), (float)y +2, -1, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics){
        int xModifier = 0;
        for (int i = 0; i < this.instance.getInstance().length; i++) {
            xModifier += renderInformationPill(this.instance.getInstance()[i], guiGraphics, x + xModifier, y);
        }
    }

    private int renderInformationPill(CompoundInstance instance, GuiGraphics guiGraphics, int x, int y){
        var matterType = ((ReplicationCompoundType)instance.getType()).getMatterType();
        var color = matterType.getColor().get();
        var glitch = Minecraft.getInstance().level.random.nextDouble() < 0.001;
        var glitch2 = Minecraft.getInstance().level.random.nextDouble() < 0.003;
        var numberLength = (instance.getAmount().intValue() + "").toCharArray().length;
        RenderSystem.enableBlend();
        //RenderSystem.setShaderColor(color[0], color[1], color[2], (float) (0.25f + Math.sin(Minecraft.getInstance().level.getGameTime() / 12f)) / 6f);
        RenderSystem.setShaderColor(color[0], color[1], color[2], color[3]);
        /*STYLE 1 guiGraphics.blit(new ResourceLocation(Replication.MOD_ID, "textures/gui/mattertypes/" + matterType.getName().toLowerCase() + ".png"), x + (glitch ? 1 : 0), y +(glitch ? 1 : 0), 0,0, 16, 16, 16, 16);
        float size = 0.45f;
        guiGraphics.blit(BAR, x + (glitch ? 1 : 0), y +(glitch ? 1 : 0) +17, 40 * size,40 * size, (int) (38 * size), (int) (16 * size), (int) (128 * size), (int) (128 * size));
        RenderSystem.setShaderColor(1,1,1,1);
        guiGraphics.pose().pushPose();
        float scale = 0.5f;
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf((int)Math.ceil(instance.getAmount())), (int) ((x + (glitch ? 1 : 0) +9 ) / scale) - Minecraft.getInstance().font.width(String.valueOf((int)Math.ceil(instance.getAmount()))) / 2, (int) ((y +(glitch ? 1 : 0) +18) / scale), new Color(color[0], color[1], color[2], color[3]).getRGB());
        guiGraphics.pose().popPose();*/
        guiGraphics.blit(new ResourceLocation(Replication.MOD_ID, "textures/gui/mattertypes/" + matterType.getName().toLowerCase() + ".png"), x + (glitch ? 1 : 0), y +(glitch ? 1 : 0), 0,0, 16, 16, 16, 16);
        float size = 1;
        guiGraphics.blit(BAR, x + (glitch ? 1 : 0) -1, y +(glitch ? 1 : 0) -1, 60 * size,57 * size, 18, 22, (int) (128 * size), (int) (128 * size));
        RenderSystem.setShaderColor(1,1,1,1);
        guiGraphics.pose().pushPose();
        float scale = 0.5f;
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(Mth.ceil(instance.getAmount())), (int) ((x + (glitch ? 1 : 0) +8 ) / scale), (int) ((y +(glitch ? 1 : 0) +16) / scale), new Color(color[0], color[1], color[2], color[3]).getRGB());
        guiGraphics.pose().popPose();
        /*OLD ENCODED NUMBERS guiGraphics.blit(BAR, x + (glitch ? 1 : 0), y +(glitch ? 1 : 0), 0,56, 3, 15, 128, 128);
        for (int i = 0; i < numberLength * 10 + 1; i++) {
            guiGraphics.blit(BAR, x + (glitch ? 1 : 0) + i + 3, y +(glitch ? 1 : 0), 3,56, 1, 15, 128, 128);

        }
        guiGraphics.blit(BAR, x + (glitch ? 1 : 0) + 3 + numberLength*10 +1, y +(glitch ? 1 : 0),  35,56, 3, 15, 128, 128);
        RenderSystem.setShaderColor(color[0], color[1], color[2], color[3]);
        guiGraphics.blit(BAR, x + (glitch ? 1 : 0), y +(glitch ? 1 : 0), !glitch2 ? 40 : 0,40, 3, 15, 128, 128);
        for (int i = 0; i < numberLength * 10 + 1; i++) {
            guiGraphics.blit(BAR, x + (glitch ? 1 : 0) + i + 3, y +(glitch ? 1 : 0), 3,40, 1, 15, 128, 128);

        }
        guiGraphics.blit(BAR, x + (glitch ? 1 : 0) + 3 + numberLength*10 +1, y +(glitch ? 1 : 0), !glitch2 ? 75 : 35,40, 3, 15, 128, 128);
        renderEncodedTextNumber( instance.getAmount().intValue() + "", x + 4, y + 3 + (glitch ? 1 : 0), guiGraphics);*/
        RenderSystem.setShaderColor(1,1,1,1);

        RenderSystem.disableBlend();
        return 19;
    }

    private void renderEncodedTextNumber(String number, int x, int y, GuiGraphics guiGraphics){
        for (int i = 0; i < number.toCharArray().length; i++) {
            int parseInt = Integer.parseInt(number.toCharArray()[i] + "");
            guiGraphics.blit(BAR, (int) (x + i * 10), y, parseInt * 9,27, 9, 9, 128, 128);
        }
    }
}
