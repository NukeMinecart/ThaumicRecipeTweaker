package nukeminecart.thaumicrecipe.gui.lists;

import nukeminecart.thaumicrecipe.Config;
import nukeminecart.thaumicrecipe.recipes.file.FileParser;

import java.io.File;
import java.io.IOException;

import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.MOD_ID;
import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.recipeDirectory;
import static nukeminecart.thaumicrecipe.gui.lists.ListRetriever.*;

public class ListUpdater {
    private static File ingredientsFile;
    private static File aspectsFile;
    private static File researchFile;

    private static File recipesFile;

    /**
     * Update all the lst {@link File} in the thaumicrecioe directory
     */
    public static void updateListFiles() throws IOException {
        createListFiles();
        getListsFromRegistries();
        if (Config.shouldGUIOpen) {
            FileParser.saveRecipesToFile(recipesFile, recipeList);
            FileParser.saveToFile(ingredientsFile, itemList.keySet(), false);
            FileParser.saveToFile(aspectsFile, aspectList.keySet(), false);
            FileParser.saveToFile(researchFile, researchList.keySet(), false);
        }
    }

    /**
     * Create any missing lst {@link File}
     */
    private static void createListFiles() throws IOException {
        ingredientsFile = new File(recipeDirectory, "ingredients.lst");
        if (ingredientsFile.createNewFile()) System.out.println(MOD_ID + ": Ingredients File Created Successfully");

        aspectsFile = new File(recipeDirectory, "aspects.lst");
        if (!aspectsFile.createNewFile()) System.out.println(MOD_ID + ": Aspects File Created Successfully");

        researchFile = new File(recipeDirectory, "research.lst");
        if (!researchFile.createNewFile()) System.out.println(MOD_ID + ": Research File Created Successfully");

        recipesFile = new File(recipeDirectory, "recipes.lst");
        if (!recipesFile.createNewFile()) System.out.println(MOD_ID + ": Recipes File Created Successfully");

    }
}
