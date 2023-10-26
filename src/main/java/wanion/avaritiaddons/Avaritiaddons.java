package wanion.avaritiaddons;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import static wanion.avaritiaddons.common.Reference.CLIENT_PROXY;
import static wanion.avaritiaddons.common.Reference.DEPENDENCIES;
import static wanion.avaritiaddons.common.Reference.MOD_ID;
import static wanion.avaritiaddons.common.Reference.MOD_NAME;
import static wanion.avaritiaddons.common.Reference.MOD_VERSION;
import static wanion.avaritiaddons.common.Reference.SERVER_PROXY;

import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.block.chest.infinity.BlockInfinityChest;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = DEPENDENCIES)
public final class Avaritiaddons {

    @Mod.Instance
    public static Avaritiaddons instance;

    public static SimpleNetworkWrapper networkWrapper;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    public static final int GUI_ID_COMPRESSED_CHEST = 0, GUI_ID_INFINITY_CHEST = 1, GUI_ID_EXTREME_AUTO_CRAFTER = 2;

    public static final CreativeTabs creativeTabs = new CreativeTabs(MOD_ID) {

        @SideOnly(Side.CLIENT)
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BlockInfinityChest.instance);
        }
    };

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @NetworkCheckHandler
    public boolean matchModVersions(final Map<String, String> remoteVersions, final Side side) {
        return side == Side.CLIENT ? remoteVersions.containsKey(MOD_ID)
                : !remoteVersions.containsKey(MOD_ID) || remoteVersions.get(MOD_ID).equals(MOD_VERSION);
    }
}
