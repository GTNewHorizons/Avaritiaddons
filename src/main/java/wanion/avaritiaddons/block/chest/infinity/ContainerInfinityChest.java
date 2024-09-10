package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.CommonProxy;
import wanion.avaritiaddons.block.chest.ContainerAvaritiaddonsChest;
import wanion.avaritiaddons.network.InfinityChestSlotSync;

public final class ContainerInfinityChest extends ContainerAvaritiaddonsChest {

    public ContainerInfinityChest(@Nonnull final TileEntityInfinityChest tileEntityCompressedChest,
            final InventoryPlayer inventoryPlayer) {
        super(tileEntityCompressedChest, inventoryPlayer);
    }

    @Override
    public boolean mergeItemStack(final ItemStack itemStack, final int start, final int end, final boolean backwards) {
        if (CommonProxy.isClient()) return false;
        int currentSlot = !backwards ? start : end - 1;
        boolean someThingChanged = false;

        while (itemStack.stackSize > 0 && (!backwards && currentSlot < end || backwards && currentSlot >= start)) {
            final Slot slot = (Slot) inventorySlots.get(currentSlot);
            final ItemStack slotStack = slot.getStack();
            boolean changed = false;

            if (!backwards) {
                if (slotStack == null) {
                    slot.putStack(itemStack.copy());
                    itemStack.stackSize = 0;
                    changed = true;
                } else if (slotStack.getItem() == itemStack.getItem()
                        && itemStack.getItemDamage() == slotStack.getItemDamage()
                        && ItemStack.areItemStackTagsEqual(itemStack, slotStack)) {
                            if ((long) slotStack.stackSize + (long) itemStack.stackSize <= (long) Integer.MAX_VALUE) {
                                slotStack.stackSize += itemStack.stackSize;
                                itemStack.stackSize = 0;
                                changed = true;
                            }
                        }
                ++currentSlot;
            } else {
                if (slotStack == null) {
                    final int dif = MathHelper.clamp_int(itemStack.getMaxStackSize(), 1, itemStack.stackSize);
                    final ItemStack newItemStack = itemStack.copy();
                    newItemStack.stackSize = dif;
                    itemStack.stackSize -= dif;
                    slot.putStack(newItemStack);
                    changed = true;
                } else
                    if (slotStack.getItem() == itemStack.getItem() && slotStack.stackSize < slotStack.getMaxStackSize()
                            && itemStack.getItemDamage() == slotStack.getItemDamage()
                            && ItemStack.areItemStackTagsEqual(itemStack, slotStack)) {
                                final int dif = MathHelper.clamp_int(
                                        itemStack.stackSize,
                                        1,
                                        slotStack.getMaxStackSize() - slotStack.stackSize);
                                slotStack.stackSize += dif;
                                itemStack.stackSize -= dif;
                                changed = true;
                            }
                --currentSlot;
            }
            if (changed) {
                slot.onSlotChanged();
                someThingChanged = true;
            }
        }
        return someThingChanged;
    }

    @Override
    public boolean canDragIntoSlot(final Slot slot) {
        return slot.slotNumber > 242;
    }

    @Override
    public ItemStack slotClick(int slot, final int mouseButton, final int modifier, final EntityPlayer entityPlayer) {
        if (slot >= 0 && slot < 243 && modifier == 0) {
            Slot actualSlot = (Slot) inventorySlots.get(slot);
            final ItemStack slotStack = actualSlot.getStack();
            final ItemStack playerStack = entityPlayer.inventory.getItemStack();
            if (mouseButton == 0) {
                if (slotStack == null && playerStack != null) {
                    actualSlot.putStack(playerStack.copy());
                    entityPlayer.inventory.setItemStack(null);
                } else if (slotStack != null && playerStack == null) {
                    final ItemStack newItemStack = slotStack.copy();
                    int took = MathHelper.clamp_int(slotStack.stackSize, 1, newItemStack.getMaxStackSize());
                    newItemStack.stackSize = took;
                    slotStack.stackSize -= took;
                    entityPlayer.inventory.setItemStack(newItemStack);
                    if (slotStack.stackSize == 0) actualSlot.putStack(null);
                    else actualSlot.onSlotChanged();
                } else if (slotStack != null && slotStack.getItem() == playerStack.getItem()
                        && playerStack.getItemDamage() == slotStack.getItemDamage()
                        && ItemStack.areItemStackTagsEqual(playerStack, slotStack)) {
                            if ((long) slotStack.stackSize + (long) playerStack.stackSize <= (long) Integer.MAX_VALUE) {
                                slotStack.stackSize += playerStack.stackSize;
                                entityPlayer.inventory.setItemStack(null);
                            }
                        }
            } else if (mouseButton == 1 && playerStack != null) {
                if (slotStack == null) {
                    playerStack.stackSize--;
                    final ItemStack newItemStack = playerStack.copy();
                    newItemStack.stackSize = 1;
                    actualSlot.putStack(newItemStack);
                    if (playerStack.stackSize == 0) entityPlayer.inventory.setItemStack(null);
                } else if (slotStack.getItem() == playerStack.getItem()
                        && playerStack.getItemDamage() == slotStack.getItemDamage()
                        && ItemStack.areItemStackTagsEqual(playerStack, slotStack)) {
                            playerStack.stackSize--;
                            slotStack.stackSize++;
                            actualSlot.onSlotChanged();
                            if (playerStack.stackSize == 0) entityPlayer.inventory.setItemStack(null);
                        }
            } else if (mouseButton == 1 && slotStack != null) {
                int took = slotStack.stackSize > slotStack.getMaxStackSize() ? slotStack.getMaxStackSize() / 2
                        : slotStack.stackSize / 2;
                if (took == 0) took = 1;
                final ItemStack newItemStack = slotStack.copy();
                newItemStack.stackSize = took;
                slotStack.stackSize -= took;
                entityPlayer.inventory.setItemStack(newItemStack);
                if (slotStack.stackSize == 0) actualSlot.putStack(null);
                else actualSlot.onSlotChanged();
            }
            return actualSlot.getHasStack() ? actualSlot.getStack().copy() : null;
        } else if (slot >= 0 && slot < 243 && modifier == 2) return null;
        else return super.slotClick(slot, mouseButton, modifier, entityPlayer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void detectAndSendChanges() {
        if (crafters.isEmpty()) return;
        ICrafting bCrafting;
        for (int i = 0; i < inventorySlots.size(); ++i) {
            ItemStack itemstack = ((Slot) inventorySlots.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack) inventoryItemStacks.get(i);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                itemstack1 = itemstack == null ? null : itemstack.copy();
                inventoryItemStacks.set(i, itemstack1);

                final NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setShort("Slot", (short) i);
                if (itemstack1 != null) nbtTagCompound.setInteger("intCount", itemstack1.stackSize);
                for (Object crafter : crafters)
                    if ((bCrafting = (ICrafting) crafter) instanceof EntityPlayerMP) Avaritiaddons.networkWrapper
                            .sendTo(new InfinityChestSlotSync(itemstack1, i), (EntityPlayerMP) bCrafting);
            }
        }
    }

    public void syncData(final ItemStack itemStack, final int slot, final int stackSize) {
        if (itemStack != null) {
            itemStack.stackSize = stackSize;
            ((Slot) inventorySlots.get(slot)).putStack(itemStack);
        } else((Slot) inventorySlots.get(slot)).putStack(null);
    }
}
