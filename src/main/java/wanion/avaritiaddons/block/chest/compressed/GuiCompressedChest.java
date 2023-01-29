package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.InventoryPlayer;

import wanion.avaritiaddons.block.chest.GuiAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;

public final class GuiCompressedChest extends GuiAvaritiaddonsChest {

    private final TileEntityCompressedChest tileEntityCompressedChest;

    public GuiCompressedChest(@Nonnull final TileEntityCompressedChest tileEntityCompressedChest,
            final InventoryPlayer inventoryPlayer) {
        super(new ContainerCompressedChest(tileEntityCompressedChest, inventoryPlayer));
        this.tileEntityCompressedChest = tileEntityCompressedChest;
    }

    @Nonnull
    @Override
    protected TileEntityAvaritiaddonsChest getTileEntity() {
        return tileEntityCompressedChest;
    }
}
