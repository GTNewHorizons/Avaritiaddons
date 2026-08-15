package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class InventoryAvaritiaddonsChest implements IInventory {

    private final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest;
    private final int maxStackSize;
    public final ItemStack[] contents = new ItemStack[243];

    public InventoryAvaritiaddonsChest(@Nonnull final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest,
            final int maxStackSize) {
        this.tileEntityAvaritiaddonsChest = tileEntityAvaritiaddonsChest;
        this.maxStackSize = maxStackSize;
    }

    @Override
    public int getSizeInventory() {
        return 243;
    }

    @Override
    public ItemStack getStackInSlot(final int slot) {
        return contents[slot];
    }

    @Override
    public ItemStack decrStackSize(final int slot, final int howMany) {
        final ItemStack slotStack = contents[slot];
        if (slotStack == null) return null;

        final int toExtract = Math.min(howMany, slotStack.stackSize);
        if (toExtract <= 0) return null;

        if (slotStack.stackSize == toExtract) {
            // Avoid copying of the stack
            contents[slot] = null;
            return slotStack;
        }

        final ItemStack extracted = slotStack.copy();
        extracted.stackSize = toExtract;
        slotStack.stackSize -= toExtract;

        markDirty();
        return extracted;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slot) {
        return contents[slot];
    }

    @Override
    public void setInventorySlotContents(final int slot, final ItemStack itemStack) {
        contents[slot] = itemStack;
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return tileEntityAvaritiaddonsChest.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return tileEntityAvaritiaddonsChest.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return maxStackSize;
    }

    @Override
    public void markDirty() {
        tileEntityAvaritiaddonsChest.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
        return true;
    }
}
