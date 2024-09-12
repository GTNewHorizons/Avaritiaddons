package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import wanion.avaritiaddons.Avaritiaddons;

public abstract class BlockAvaritiaddonsChest extends BlockContainer {

    public BlockAvaritiaddonsChest(@Nonnull final Material material) {
        super(material);
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        setCreativeTab(Avaritiaddons.creativeTabs);
    }

    @Override
    public final boolean isOpaqueCube() {
        return false;
    }

    @Override
    public final boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public final int getRenderType() {
        return -1;
    }

    @Override
    public final void breakBlock(final World world, final int x, final int y, final int z, final Block block,
            final int metadata) {
        super.breakBlock(world, x, y, z, block, metadata);
        world.func_147453_f(x, y, z, block);
    }

    @Override
    public final void onBlockPlacedBy(final World world, final int x, final int y, final int z,
            final EntityLivingBase entity, final ItemStack itemStack) {
        final int side = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        final TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest = (TileEntityAvaritiaddonsChest) world
                .getTileEntity(x, y, z);
        if (tileEntityAvaritiaddonsChest != null) {
            tileEntityAvaritiaddonsChest
                    .setFacingSide(side == 0 ? 180 : side == 1 ? -90 : side == 2 ? 0 : side == 3 ? 90 : 0);
            if (itemStack.stackTagCompound != null)
                tileEntityAvaritiaddonsChest.readCustomNBT(itemStack.stackTagCompound);
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        if (willHarvest) {
            for (ItemStack drop : getDrops(world, x, y, z, 0, 0)) {
                dropBlockAsItem(world, x, y, z, drop);
            }
        }
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        TileEntity te = world.getTileEntity(x, y, z);
        Block block = world.getBlock(x, y, z);
        ArrayList<ItemStack> list = new ArrayList<>();
        if (te instanceof TileEntityAvaritiaddonsChest) {
            TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest = (TileEntityAvaritiaddonsChest) te;
            final ItemStack droppedStack = new ItemStack(block, 1, 0);
            droppedStack.setTagCompound(tileEntityAvaritiaddonsChest.writeCustomNBT(new NBTTagCompound()));
            list.add(droppedStack);
        }
        return list;
    }

}
