package nukeminecart.thaumicrecipe;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod(modid = ThaumicRecipeTweaker.MODID, useMetadata = true)
public class ThaumicRecipeTweaker
{
    public static final String MODID = "thaumicrecipe";
    public static final String GUIID = "ThaumicRecipeTweakerGUI";
    public static final String VERSION = "1.0.0";

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(Config.shouldGUIOpen) {
            URL directoryURL = DirectoryLocator.getLocation(ThaumicRecipeTweaker.class);
            String directory = DirectoryLocator.urlToFile(directoryURL).getPath().replace("ThaumicRecipeTweaker-" + VERSION + ".jar", "");
            new JarExecutor().executeJar((directory + ThaumicRecipeTweaker.GUIID + ".jar"), Minecraft.getMinecraft().mcDataDir.getPath(), Config.loadedRecipeFile);
        }
        List<Map<String, ResearchEntry>> researchEntries = new ArrayList<>();
        List<String> researchKeys = new ArrayList<>();
        for(ResearchCategory category: ResearchCategories.researchCategories.values()){
            researchEntries.add(category.research);
        }
        for(Map<String, ResearchEntry> entry : researchEntries){
            for(ResearchEntry researchEntry : entry.values()){
                researchKeys.add(researchEntry.getKey());
            }
        }
    }
}
