package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import wanion.avaritiaddons.common.Reference;

public abstract class GuiAvaritiaddonsChest extends GuiContainer {

    private final static ResourceLocation avaritiaddonsChestGui = new ResourceLocation(
            Reference.MOD_ID,
            "textures/gui/avaritiaddonsChest.png");

    public GuiAvaritiaddonsChest(@Nonnull final Container container) {
        super(container);
        xSize = 500;
        ySize = 276;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(avaritiaddonsChestGui);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(guiLeft, guiTop, 0, 0.0, 0.0);
        tessellator.addVertexWithUV(guiLeft, guiTop + xSize, 0, 0.0, 1.0);
        tessellator.addVertexWithUV(guiLeft + xSize, guiTop + xSize, 0, 1.0, 1.0);
        tessellator.addVertexWithUV(guiLeft + xSize, guiTop, 0, 1.0, 0.0);
        tessellator.draw();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
        fontRendererObj.drawString(I18n.format(getTileEntity().getInventoryName()), 7, 7, 0x404040);
        fontRendererObj.drawString(I18n.format("container.inventory"), 169, 183, 0x404040);
    }

    @Nonnull
    protected abstract TileEntityAvaritiaddonsChest getTileEntity();
}
