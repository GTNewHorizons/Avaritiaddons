package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.block.chest.TileEntityAvaritiaddonsChest;
import wanion.avaritiaddons.client.ClientConstants;

public final class TileEntityInfinityChest extends TileEntityAvaritiaddonsChest {

    public TileEntityInfinityChest() {
        super(Integer.MAX_VALUE);
    }

    @Override
    public void setInventorySlotContents(int slot, final ItemStack itemStack) {
        if (itemStack == null) {
            inventoryAvaritiaddonsChest.contents[slot] = null;
            markDirty();
            return;
        }
        final int perfectSlot = findSlotFor(itemStack);
        if ((slot >= 243 && perfectSlot == -1) || itemStack.stackSize < 0) return;
        if (slot < 243) {
            final ItemStack slotStack = inventoryAvaritiaddonsChest.contents[slot];
            if (isSameItem(slotStack, itemStack)) {
                int tmp = itemStack.stackSize;
                itemStack.stackSize = slotStack.stackSize;
                slotStack.stackSize = tmp;
                markDirty();
                return;
            }
        }
        if (perfectSlot != -1 && perfectSlot != slot) {
            slot = perfectSlot;
            if (inventoryAvaritiaddonsChest.contents[slot] != null) {
                inventoryAvaritiaddonsChest.contents[slot].stackSize += itemStack.stackSize;
                itemStack.stackSize = 0;
            } else inventoryAvaritiaddonsChest.contents[slot] = itemStack;
        } else if (perfectSlot != -1) {
            final ItemStack slotStack = inventoryAvaritiaddonsChest.contents[slot];
            int tmp = itemStack.stackSize;
            itemStack.stackSize = slotStack.stackSize;
            slotStack.stackSize = tmp;
            if (slotStack.stackSize <= 0) inventoryAvaritiaddonsChest.contents[slot] = null;
        } else {
            inventoryAvaritiaddonsChest.contents[slot] = itemStack;
        }
        markDirty();
    }

    private boolean isSameItem(ItemStack slotStack, ItemStack itemStack) {
        return slotStack != null && itemStack != null
                && slotStack.getItem() == itemStack.getItem()
                && itemStack.getItemDamage() == slotStack.getItemDamage()
                && ItemStack.areItemStackTagsEqual(itemStack, slotStack);
    }

    private int findSlotFor(@Nonnull final ItemStack itemStack) {
        for (int i = 0; i < 243; i++) {
            final ItemStack slotStack = inventoryAvaritiaddonsChest.contents[i];
            if (isSameItem(slotStack, itemStack)) {
                if ((long) slotStack.stackSize + (long) itemStack.stackSize <= (long) Integer.MAX_VALUE) return i;
            }
        }
        return -1;
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbtTagCompound) {
        final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
            final int slot = slotCompound.getShort("Slot");
            if (slot >= 0 && slot < 243) {
                inventoryAvaritiaddonsChest.setInventorySlotContents(slot, readItemStackFromNbt(slotCompound));
            }
        }
    }

    private static ItemStack readItemStackFromNbt(final NBTTagCompound nbtTagCompound) {
        final ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbtTagCompound);
        if (itemStack == null) {
            // might be null if an item that was stored in a chest
            // gets removed from the game when updating / removing a mod
            return null;
        }
        itemStack.stackSize = nbtTagCompound.getInteger("intCount");
        if (nbtTagCompound.hasKey("tag")) itemStack.setTagCompound(nbtTagCompound.getCompoundTag("tag"));
        return itemStack;
    }

    @Override
    public NBTTagCompound writeCustomNBT(final NBTTagCompound nbtTagCompound) {
        final NBTTagList nbtTagList = new NBTTagList();
        boolean hasItems = false;
        for (int i = 0; i < 243; i++) {
            final ItemStack itemStack = inventoryAvaritiaddonsChest.getStackInSlot(i);
            if (itemStack != null) {
                final NBTTagCompound slotCompound = new NBTTagCompound();
                slotCompound.setShort("Slot", (short) i);
                nbtTagList.appendTag(writeItemStackToNbt(slotCompound, itemStack));
                hasItems = true;
            }
        }
        if (hasItems) nbtTagCompound.setTag("Contents", nbtTagList);
        if (nbtTagCompound.hasNoTags()) return null;
        return nbtTagCompound;
    }

    private static NBTTagCompound writeItemStackToNbt(@Nonnull final NBTTagCompound nbtTagCompound,
            @Nonnull final ItemStack itemStack) {
        itemStack.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("intCount", itemStack.stackSize);
        if (itemStack.stackTagCompound != null) nbtTagCompound.setTag("tag", itemStack.stackTagCompound);
        return nbtTagCompound;
    }

    @Override
    public ItemStack getStackInSlot(final int slot) {
        return slot <= 242 ? inventoryAvaritiaddonsChest.getStackInSlot(slot) : null;
    }

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
        return slot != 243 || findSlotFor(itemStack) != -1;
    }

    @Override
    public int getSizeInventory() {
        return 244;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    protected ResourceLocation getTexture() {
        return ClientConstants.INFINITY_CHEST_ANIMATION.getCurrentFrame();
    }

    @Override
    public String getInventoryName() {
        return "container.InfinityChest";
    }
}
