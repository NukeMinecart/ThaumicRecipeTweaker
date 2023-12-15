package nukeminecart.thaumicrecipe;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nukeminecart.thaumicrecipe.recipes.file.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.MOD_ID;
import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.minecraftDirectory;

@net.minecraftforge.common.config.Config(modid = ThaumicRecipeTweaker.MODID)
public class Config {

    @net.minecraftforge.common.config.Config.Comment({"Change this to change the recipe being loaded", "All recipes must be stored in the thaumic recipe folder and must contain the .rcp file extension"})
    @net.minecraftforge.common.config.Config.RequiresMcRestart()
    public static String loadedRecipeFile = "";

    @net.minecraftforge.common.config.Config.Comment({"Set this to true to have the Thaumic Recipe Tweaker GUI open or false to force the GUI to not open \n requires ThaumicRecipeTweakerGUI.jar to be installed (placed in the mods folder)"})
    @net.minecraftforge.common.config.Config.RequiresMcRestart()
    public static Boolean shouldGUIOpen = true;

    /**
     * Updates the configuration {@link File} with values from the thaumicrecipetweakerGUI configuration {@link File}
     */
    public static void updateConfig() {
        HashMap<String, String> configuration;
        try {
            configuration = FileParser.loadConfigOptions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String key : configuration.keySet()) {
            if (key.equals("open-gui")) shouldGUIOpen = Boolean.valueOf(configuration.get(key));
            if (key.equals("active-recipe")) loadedRecipeFile = configuration.get(key);
        }
        List<String> contents;
        File configFile = new File(minecraftDirectory, MOD_ID + ".cfg");
        try {
            contents = FileParser.readFile(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < contents.size(); i++) {
            String content = contents.get(i);
            if (content.contains("shouldGUIOpen")) content = content.split("=")[0] + "=" + shouldGUIOpen;
            if (content.contains("loadedRecipeFile")) content = content.split("=")[0] + "=" + loadedRecipeFile;
            contents.set(i, content);
        }
        try {
            FileParser.saveToFile(configFile, contents, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Mod.EventBusSubscriber(modid = ThaumicRecipeTweaker.MODID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ThaumicRecipeTweaker.MODID)) {
                ConfigManager.sync(ThaumicRecipeTweaker.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
            }
        }
    }
}

