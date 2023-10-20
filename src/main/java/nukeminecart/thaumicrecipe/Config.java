package nukeminecart.thaumicrecipe;

import net.minecraftforge.common.config.Configuration;
import nukeminecart.thaumicrecipe.proxy.ServerProxy;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    // This values below you can access elsewhere in your mod:
    public static String currentlyLoadedRecipe = "";

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = ServerProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception ignored) {
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        currentlyLoadedRecipe = cfg.get(CATEGORY_GENERAL,"LoadedRecipe","none").getString();

    }

}