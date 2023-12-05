package nukeminecart.thaumicrecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.Ingredient;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class ThaumicRecipeConstants {
    public static File recipeDirectory, minecraftDirectory;
    public static String stringArraySeparator, stringSeparator, MOD_ID, separator, mapSeparator;
    public static ConcurrentHashMap<String, Ingredient> reverseIngredientMap;


    public static void initConstants() {
        minecraftDirectory = Minecraft.getMinecraft().mcDataDir;
        MOD_ID = "thaumicrecipe";
        separator = System.getProperty("file.separator");
        recipeDirectory = new File(minecraftDirectory + separator + MOD_ID + separator);
        stringSeparator = "-=-";
        stringArraySeparator = "-";
        mapSeparator = ":";
        reverseIngredientMap = new ConcurrentHashMap<>();
    }
}
