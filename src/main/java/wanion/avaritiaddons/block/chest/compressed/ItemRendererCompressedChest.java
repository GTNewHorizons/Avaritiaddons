package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.block.chest.RendererAvaritiaddonsChest;

@SideOnly(Side.CLIENT)
public final class ItemRendererCompressedChest implements IItemRenderer {

    private static final TileEntityCompressedChest tileEntityCompressedChest = new TileEntityCompressedChest();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.ENTITY)
            RendererAvaritiaddonsChest.instance.renderTileEntityAt(tileEntityCompressedChest, -0.5F, -0.5F, -0.5F, 0);
        else RendererAvaritiaddonsChest.instance.renderTileEntityAt(tileEntityCompressedChest, 0, 0, 0, 0);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
}
