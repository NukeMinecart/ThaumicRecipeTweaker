package nukeminecart.thaumicrecipe;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nukeminecart.thaumicrecipe.recipes.file.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@net.minecraftforge.common.config.Config(modid = ThaumicRecipeTweaker.MODID)
public class Config {

    @net.minecraftforge.common.config.Config.Comment({"Change this to change the recipe being loaded", "All recipes must be stored in the thaumic recipe folder and must contain the .rcp file extension"})
    @net.minecraftforge.common.config.Config.RequiresMcRestart()
    public static String loadedRecipeFile = "";

    @net.minecraftforge.common.config.Config.Comment({"Set this to true to have the Thaumic Recipe Tweaker GUI open or false to force the GUI to not open \n requires ThaumicRecipeTweakerGUI.jar to be installed (placed in the mods folder)"})
    @net.minecraftforge.common.config.Config.RequiresMcRestart()
    public static Boolean shouldGUIOpen = true;

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

    /**
     * Updates the configuration {@link File} with values from the thaumicrecipetweakerGUI configuration {@link File}
     */
    public static void updateConfig(){
        HashMap<String, String> configuration;
        try {
            configuration = FileParser.loadConfigOptions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String temp = "";
        for (String key : configuration.keySet()){
            if(key.equals("open-gui")) shouldGUIOpen = Boolean.valueOf(configuration.get(key));
            if(key.equals("active-recipe")) loadedRecipeFile = configuration.get(key);
            temp = shouldGUIOpen+loadedRecipeFile;
        }
        //TODO FIX CONFIGURATION FILE NOT SAVING IN THE SYNC METHOD -> SAVE MANUALLY PROB
        ConfigManager.sync(ThaumicRecipeTweaker.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
        throw new NullPointerException(temp+shouldGUIOpen+loadedRecipeFile+configuration);

    }
}

