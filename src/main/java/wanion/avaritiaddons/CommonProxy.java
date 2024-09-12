package wanion.avaritiaddons;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import static fox.spiteful.avaritia.Config.craftingOnly;
import static wanion.avaritiaddons.Avaritiaddons.networkWrapper;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.Grinder;
import fox.spiteful.avaritia.items.LudicrousItems;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import wanion.avaritiaddons.block.chest.ItemBlockAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.compressed.BlockCompressedChest;
import wanion.avaritiaddons.block.chest.compressed.TileEntityCompressedChest;
import wanion.avaritiaddons.block.chest.infinity.BlockInfinityChest;
import wanion.avaritiaddons.block.chest.infinity.TileEntityInfinityChest;
import wanion.avaritiaddons.block.extremeautocrafter.BlockExtremeAutoCrafter;
import wanion.avaritiaddons.block.extremeautocrafter.TileEntityExtremeAutoCrafter;
import wanion.avaritiaddons.core.GuiHandler;
import wanion.avaritiaddons.network.InfinityChestClick;
import wanion.avaritiaddons.network.InfinityChestConfirmation;
import wanion.avaritiaddons.network.InfinityChestSlotSync;
import wanion.avaritiaddons.network.InfinityChestSyncAllSlots;
import wanion.lib.common.MetaItem;

public class CommonProxy {

    private final TIntSet oresToRemove = new TIntHashSet();
    private final TIntSet stacksToRemove = new TIntHashSet();

    public EntityPlayer getPlayerEntity(final MessageContext context) {
        return context.getServerHandler().playerEntity;
    }

    public void preInit() {
        GameRegistry.registerBlock(BlockCompressedChest.instance, ItemBlockAvaritiaddonsChest.class, "CompressedChest");
        GameRegistry.registerBlock(BlockInfinityChest.instance, ItemBlockAvaritiaddonsChest.class, "InfinityChest");
        GameRegistry.registerBlock(BlockExtremeAutoCrafter.instance, "ExtremeAutoCrafter");
        GameRegistry.registerTileEntity(TileEntityCompressedChest.class, "Avaritiaddons:CompressedChest");
        GameRegistry.registerTileEntity(TileEntityInfinityChest.class, "Avaritiaddons:InfinityChest");
        GameRegistry.registerTileEntity(TileEntityExtremeAutoCrafter.class, "Avaritiaddons:ExtremeAutoCrafter");
        NetworkRegistry.INSTANCE.registerGuiHandler(Avaritiaddons.instance, GuiHandler.instance);
        networkWrapper
                .registerMessage(InfinityChestSlotSync.Handler.class, InfinityChestSlotSync.class, 0, Side.SERVER);
        networkWrapper
                .registerMessage(InfinityChestSlotSync.Handler.class, InfinityChestSlotSync.class, 1, Side.CLIENT);
        networkWrapper.registerMessage(InfinityChestClick.Handler.class, InfinityChestClick.class, 2, Side.SERVER);
        networkWrapper.registerMessage(InfinityChestClick.Handler.class, InfinityChestClick.class, 3, Side.CLIENT);
        networkWrapper.registerMessage(
                InfinityChestConfirmation.Handler.class,
                InfinityChestConfirmation.class,
                4,
                Side.SERVER);
        networkWrapper.registerMessage(
                InfinityChestConfirmation.Handler.class,
                InfinityChestConfirmation.class,
                5,
                Side.CLIENT);
        networkWrapper.registerMessage(
                InfinityChestSyncAllSlots.Handler.class,
                InfinityChestSyncAllSlots.class,
                6,
                Side.SERVER);
        networkWrapper.registerMessage(
                InfinityChestSyncAllSlots.Handler.class,
                InfinityChestSyncAllSlots.class,
                7,
                Side.CLIENT);
    }

    public final void init() {
        if (!Loader.isModLoaded("dreamcraft")) {
            if (Config.hardCompressedChestRecipe) ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(
                    new ItemStack(BlockCompressedChest.instance),
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    "CCCCCCCCC",
                    'C',
                    Blocks.chest);
            else GameRegistry.addRecipe(
                    new ShapedOreRecipe(
                            new ItemStack(BlockCompressedChest.instance),
                            "CCC",
                            "CCC",
                            "CCC",
                            'C',
                            Blocks.chest));
            if (craftingOnly) return;
            if (Config.hardInfinityChestRecipe) GameRegistry.addRecipe(
                    new ShapedOreRecipe(
                            new ItemStack(BlockInfinityChest.instance),
                            "INI",
                            "BCB",
                            "IBI",
                            'I',
                            "ingotInfinity",
                            'N',
                            "blockCosmicNeutronium",
                            'B',
                            "blockInfinity",
                            'C',
                            BlockCompressedChest.instance));
            else GameRegistry.addRecipe(
                    new ShapedOreRecipe(
                            new ItemStack(BlockInfinityChest.instance),
                            "TNT",
                            "ICI",
                            "TIT",
                            'T',
                            new ItemStack(LudicrousItems.resource, 1, 5),
                            'N',
                            "blockCosmicNeutronium",
                            'I',
                            "ingotInfinity",
                            'C',
                            BlockCompressedChest.instance));
            GameRegistry.addRecipe(
                    new ShapedOreRecipe(
                            new ItemStack(BlockExtremeAutoCrafter.instance),
                            " S ",
                            "NCN",
                            " N ",
                            'S',
                            new ItemStack(LudicrousItems.singularity, 1, 3),
                            'N',
                            "blockCosmicNeutronium",
                            'C',
                            new ItemStack(LudicrousBlocks.dire_crafting)));
        }
    }

    public final void postInit() {
        if (craftingOnly) return;
        populateRemovalSets();
        for (final Iterator<Object> catalystRecipeIterator = Grinder.catalyst.getInput()
                .iterator(); catalystRecipeIterator.hasNext();) {
            final Object catalystRecipeInput = catalystRecipeIterator.next();
            final int hash = catalystRecipeInput instanceof List ? System.identityHashCode(catalystRecipeInput)
                    : catalystRecipeInput instanceof ItemStack ? MetaItem.get((ItemStack) catalystRecipeInput) : 0;
            if (hash == 0) continue;
            if ((catalystRecipeInput instanceof List && oresToRemove.contains(hash))
                    || (catalystRecipeInput instanceof ItemStack && stacksToRemove.contains(hash)))
                catalystRecipeIterator.remove();
        }
    }

    private void populateRemovalSets() {
        Config.thingsToRemoveFromInfinityCatalystRecipe.forEach(toRemove -> {
            if (toRemove.indexOf(':') == -1) {
                final List<ItemStack> ores = OreDictionary.getOres(toRemove, false);
                if (ores != null && !ores.isEmpty()) oresToRemove.add(System.identityHashCode(ores));
            } else {
                final int separatorChar = toRemove.indexOf('#');
                final Item item = MetaItem.itemRegistry
                        .getRaw(separatorChar == -1 ? toRemove : toRemove.substring(0, separatorChar));
                if (item != null) {
                    final int metadata = separatorChar == -1 ? 0
                            : Integer.parseInt(toRemove.substring(separatorChar + 1));
                    final int hash = MetaItem.get(new ItemStack(item, 1, metadata));
                    if (hash > 0) stacksToRemove.add(hash);
                }
            }
        });
    }

    public EntityPlayer getEntityPlayerFromContext(final MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public static boolean isDedicatedServer() {
        return MinecraftServer.getServer().isDedicatedServer();
    }
}
