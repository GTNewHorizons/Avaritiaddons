package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerAvaritiaddonsChest extends Container {

    public final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest;

    public ContainerAvaritiaddonsChest(@Nonnull final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest,
            final InventoryPlayer inventoryPlayer) {
        for (int y = 0; y < 9; y++) for (int x = 0; x < 27; x++)
            addSlotToContainer(new Slot(tileEntityAvaritiaddonsChest, y * 27 + x, 8 + (18 * x), 18 + (18 * y)));
        for (int y = 0; y < 3; y++) for (int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(inventoryPlayer, 9 + y * 9 + x, 170 + (18 * x), 194 + (18 * y)));
        for (int i = 0; i < 9; i++) addSlotToContainer(new Slot(inventoryPlayer, i, 170 + (18 * i), 252));
        (this.tileEntityAvaritiaddonsChest = tileEntityAvaritiaddonsChest).openInventory();
    }

    @Override
    public final boolean canInteractWith(final EntityPlayer entityPlayer) {
        return tileEntityAvaritiaddonsChest.isUseableByPlayer(entityPlayer);
    }

    @Override
    public final ItemStack transferStackInSlot(final EntityPlayer entityPlayer, final int slot) {
        ItemStack itemstack = null;
        final Slot actualSlot = (Slot) this.inventorySlots.get(slot);
        if (actualSlot != null && actualSlot.getHasStack()) {
            ItemStack itemstack1 = actualSlot.getStack();
            itemstack = itemstack1.copy();
            if (slot > 242) {
                if (!mergeItemStack(itemstack1, 0, 243, false)) return null;
            } else if (!mergeItemStack(itemstack1, 243, 279, true)) return null;
            if (itemstack1.stackSize == 0) actualSlot.putStack(null);
            else actualSlot.onSlotChanged();
        }
        return itemstack;
    }

    public final void onContainerClosed(final EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        tileEntityAvaritiaddonsChest.closeInventory();
    }
}
