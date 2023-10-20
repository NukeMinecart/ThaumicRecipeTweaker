package nukeminecart.thaumicrecipe;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.net.URL;

@Mod(modid = ThaumicRecipeTweaker.MODID, useMetadata = true)
public class ThaumicRecipeTweaker
{
    public static final String MODID = "thaumicrecipe";
    public static final String GUIID = "ThaumicRecipeTweakerGUI";
    public static final String VERSION = "1.0.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        URL directoryURL = DirectoryLocator.getLocation(ThaumicRecipeTweaker.class);
        String directory = DirectoryLocator.urlToFile(directoryURL).getPath().replace("ThaumicRecipeTweaker-"+VERSION+".jar","");
        new JarExecutor().executeJar((directory+ThaumicRecipeTweaker.GUIID+".jar"),Minecraft.getMinecraft().mcDataDir.getPath(), Config.loadedRecipeFile);
    }
}
