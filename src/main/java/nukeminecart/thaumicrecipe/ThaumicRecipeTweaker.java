package nukeminecart.thaumicrecipe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nukeminecart.thaumicrecipe.gui.launcher.DirectoryLocator;
import nukeminecart.thaumicrecipe.gui.launcher.JarExecutor;
import nukeminecart.thaumicrecipe.gui.lists.ListUpdater;

import java.io.IOException;
import java.net.URL;

import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.minecraftDirectory;

@Mod(modid = ThaumicRecipeTweaker.MODID, useMetadata = true)
public class ThaumicRecipeTweaker {
    public static final String MODID = "thaumicrecipe";
    public static final String GUIID = "ThaumicRecipeTweakerGUI";
    public static final String VERSION = "1.0.0";

    /**
     * Forge post-init event where most of this {@link Mod} runs
     *
     * @param event the {@link FMLPostInitializationEvent}
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            ListUpdater.updateListFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Config.shouldGUIOpen) {
            URL directoryURL = DirectoryLocator.getLocation(ThaumicRecipeTweaker.class);
            String directory = DirectoryLocator.urlToFile(directoryURL).getPath().replace("ThaumicRecipeTweaker-" + VERSION + ".jar", "");
            new JarExecutor().executeJar((directory + ThaumicRecipeTweaker.GUIID + ".jar"), minecraftDirectory.getPath(), Config.loadedRecipeFile);
        }
        Config.updateConfig();
    }

    /**
     * Forge pre-init event where variable initialization runs
     *
     * @param event the {@link FMLPreInitializationEvent}
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ThaumicRecipeConstants.initConstants();
    }
}
