package nukeminecart.thaumicrecipe;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nukeminecart.thaumicrecipe.ui.HomeUI;

@Mod(modid = ThaumicRecipeTweaker.MODID, useMetadata = true)
public class ThaumicRecipeTweaker
{
    public static final String MODID = "thaumicrecipe";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Launch.classLoader.addTransformerExclusion("com.sun.javafx.");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        HomeUI.run();
    }
}
