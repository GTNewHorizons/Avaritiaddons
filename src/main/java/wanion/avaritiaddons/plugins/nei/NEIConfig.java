package wanion.avaritiaddons.plugins.nei;

import codechicken.nei.DefaultBookmarkContainerHandler;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import wanion.avaritiaddons.block.chest.compressed.GuiCompressedChest;
import wanion.avaritiaddons.common.Reference;

public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerBookmarkContainerHandler(GuiCompressedChest.class, new DefaultBookmarkContainerHandler());
    }

    @Override
    public String getName() {
        return Reference.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return Reference.MOD_VERSION;
    }
}
