package nukeminecart.thaumicrecipe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ThaumicRecipeTweaker.MODID, useMetadata = true)
public class ThaumicRecipeTweaker
{
    public static final String MODID = "thaumicrecipe";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        String separator = System.getProperty("file.separator");
        new JarExecutor().executeJar(System.getProperty("user.dir")+separator+"mods"+separator+"ThaumicRecipeTweakerGUI.jar");
    }
}
