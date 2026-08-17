package wanion.avaritiaddons.gametests;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;

import appeng.api.config.Actionable;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.networking.TileCreativeEnergyController;
import appeng.util.IterationCounter;
import appeng.util.item.AEItemStack;
import wanion.avaritiaddons.block.chest.infinity.TileEntityInfinityChest;
import wanion.avaritiaddons.common.Reference;

@GameTestHolder(value = Reference.MOD_ID, requiredMods = Reference.AE2_MOD_ID)
public class InfinityChestTest {

    @GameTest(template = "ae2_setup")
    public static void canExtractStacks(GameTestHelper helper) {
        TileCreativeEnergyController controller = helper
                .assertTileEntityPresent(TileCreativeEnergyController.class, helper.pos("controller"));
        helper.assertTileEntityPresent(TileEntityInfinityChest.class, helper.pos("inf_chest"));
        AENetworkProxy proxy = controller.getProxy();
        helper.startSequence().thenWaitUntil(proxy::isActive).thenExecute(() -> {
            IMEMonitor<IAEItemStack> monitor;
            try {
                monitor = proxy.getStorage().getItemInventory();
            } catch (GridAccessException e) {
                throw new RuntimeException(e);
            }
            AEItemStack dirtStack = AEItemStack.create(new ItemStack(Blocks.dirt));
            IAEItemStack getStack = monitor.getAvailableItem(dirtStack, IterationCounter.fetchNewId());
            helper.assertEquals(getStack.getStackSize(), 256);
            IAEItemStack extractStack = monitor.extractItems(
                    dirtStack.copy().setStackSize(256),
                    Actionable.MODULATE,
                    new MachineSource(controller));
            helper.assertEquals(extractStack.getStackSize(), 256);
            helper.assertInventoryEmpty(helper.pos("inf_chest"));
        }).thenSucceed();
    }

}
