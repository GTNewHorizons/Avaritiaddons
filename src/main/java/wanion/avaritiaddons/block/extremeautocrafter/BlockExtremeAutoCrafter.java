package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.Avaritiaddons;

public class BlockExtremeAutoCrafter extends BlockContainer {

    public static final BlockExtremeAutoCrafter instance = new BlockExtremeAutoCrafter();
    private static IIcon top;
    private static IIcon sides;
    private static IIcon bottom;

    private BlockExtremeAutoCrafter() {
        super(Material.iron);
        setBlockName("ExtremeAutoCrafter").setStepSound(Block.soundTypeGlass).setHardness(50.0F).setResistance(2000.0F)
                .setCreativeTab(Avaritiaddons.creativeTabs).setHarvestLevel("pickaxe", 3);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iconRegister) {
        top = iconRegister.registerIcon("avaritiaddons:extremeAutoCrafterTop");
        sides = iconRegister.registerIcon("avaritiaddons:extremeAutoCrafterSide");
        bottom = iconRegister.registerIcon("avaritiaddons:extremeAutoCrafterBottom");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        return side == 0 ? bottom : side == 1 ? top : sides;
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z,
            final EntityPlayer entityPlayer, final int side, final float hitX, final float hitY, final float hitZ) {
        if (!world.isRemote) FMLNetworkHandler.openGui(
                entityPlayer,
                Avaritiaddons.instance,
                Avaritiaddons.GUI_ID_EXTREME_AUTO_CRAFTER,
                world,
                x,
                y,
                z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int metadata) {
        return new TileEntityExtremeAutoCrafter();
    }

    @Override
    public final void breakBlock(final World world, final int x, final int y, final int z, final Block block,
            final int metadata) {
        final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter = (TileEntityExtremeAutoCrafter) world
                .getTileEntity(x, y, z);
        if (tileEntityExtremeAutoCrafter != null) {
            final ItemStack droppedStack = new ItemStack(block, 1, 0);
            droppedStack.setTagCompound(tileEntityExtremeAutoCrafter.writeCustomNBT(new NBTTagCompound()));
            world.spawnEntityInWorld(
                    new EntityItem(
                            world,
                            x + world.rand.nextFloat() * 0.8F + 0.1F,
                            y + world.rand.nextFloat() * 0.8F + 0.1F,
                            z + world.rand.nextFloat() * 0.8F + 0.1F,
                            droppedStack));
        }
        super.breakBlock(world, x, y, z, block, metadata);
        world.func_147453_f(x, y, z, block);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public final void onBlockPlacedBy(final World world, final int x, final int y, final int z,
            final EntityLivingBase entity, final ItemStack itemStack) {
        final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter = (TileEntityExtremeAutoCrafter) world
                .getTileEntity(x, y, z);
        if (tileEntityExtremeAutoCrafter != null) if (itemStack.stackTagCompound != null)
            tileEntityExtremeAutoCrafter.readCustomNBT(itemStack.stackTagCompound);
    }
}
