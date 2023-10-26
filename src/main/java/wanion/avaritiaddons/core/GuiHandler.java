package wanion.avaritiaddons.core;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.block.chest.compressed.ContainerCompressedChest;
import wanion.avaritiaddons.block.chest.compressed.GuiCompressedChest;
import wanion.avaritiaddons.block.chest.compressed.TileEntityCompressedChest;
import wanion.avaritiaddons.block.chest.infinity.ContainerInfinityChest;
import wanion.avaritiaddons.block.chest.infinity.GuiInfinityChest;
import wanion.avaritiaddons.block.chest.infinity.TileEntityInfinityChest;
import wanion.avaritiaddons.block.extremeautocrafter.ContainerExtremeAutoCrafter;
import wanion.avaritiaddons.block.extremeautocrafter.GuiExtremeAutoCrafter;
import wanion.avaritiaddons.block.extremeautocrafter.TileEntityExtremeAutoCrafter;

public final class GuiHandler implements IGuiHandler {

    public static final GuiHandler instance = new GuiHandler();

    private GuiHandler() {}

    @Override
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
            final int y, final int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null) return null;
        switch (ID) {
            case Avaritiaddons.GUI_ID_COMPRESSED_CHEST:
                if (tileEntity instanceof TileEntityCompressedChest)
                    return new ContainerCompressedChest((TileEntityCompressedChest) tileEntity, player.inventory);
            case Avaritiaddons.GUI_ID_INFINITY_CHEST:
                if (tileEntity instanceof TileEntityInfinityChest)
                    return new ContainerInfinityChest((TileEntityInfinityChest) tileEntity, player.inventory);
            case Avaritiaddons.GUI_ID_EXTREME_AUTO_CRAFTER:
                if (tileEntity instanceof TileEntityExtremeAutoCrafter)
                    return new ContainerExtremeAutoCrafter((TileEntityExtremeAutoCrafter) tileEntity, player.inventory);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
            final int y, final int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null) return null;
        switch (ID) {
            case Avaritiaddons.GUI_ID_COMPRESSED_CHEST:
                if (tileEntity instanceof TileEntityCompressedChest)
                    return new GuiCompressedChest((TileEntityCompressedChest) tileEntity, player.inventory);
            case Avaritiaddons.GUI_ID_INFINITY_CHEST:
                if (tileEntity instanceof TileEntityInfinityChest)
                    return new GuiInfinityChest((TileEntityInfinityChest) tileEntity, player.inventory);
            case Avaritiaddons.GUI_ID_EXTREME_AUTO_CRAFTER:
                if (tileEntity instanceof TileEntityExtremeAutoCrafter)
                    return new GuiExtremeAutoCrafter((TileEntityExtremeAutoCrafter) tileEntity, player.inventory);
            default:
                return null;
        }
    }
}
