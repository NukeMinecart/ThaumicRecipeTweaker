package nukeminecart.thaumicrecipe;

import net.minecraft.client.Minecraft;

import java.io.File;

public class ThaumicRecipeConstants {
    public static File recipeDirectory;
    public static File minecraftDirectory;
    public static String stringArraySeparator;
    public static String stringSeparator;
    public static String MOD_ID;
    public static String separator;

    public static void initConstants() {
        minecraftDirectory = Minecraft.getMinecraft().mcDataDir;
        MOD_ID = "thaumicrecipe";
        separator = System.getProperty("file.separator");
        recipeDirectory = new File(minecraftDirectory + separator + MOD_ID + separator);
        stringSeparator = "-=-";
        stringArraySeparator = "-";
    }
}
