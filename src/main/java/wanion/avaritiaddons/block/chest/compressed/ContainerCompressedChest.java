package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.InventoryPlayer;

import invtweaks.api.container.ChestContainer;
import wanion.avaritiaddons.block.chest.ContainerAvaritiaddonsChest;

@ChestContainer
public final class ContainerCompressedChest extends ContainerAvaritiaddonsChest {

    public ContainerCompressedChest(@Nonnull final TileEntityCompressedChest tileEntityCompressedChest,
            final InventoryPlayer inventoryPlayer) {
        super(tileEntityCompressedChest, inventoryPlayer);
    }
}
