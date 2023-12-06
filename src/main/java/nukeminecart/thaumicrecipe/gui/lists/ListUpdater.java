package nukeminecart.thaumicrecipe.gui.lists;

import nukeminecart.thaumicrecipe.recipes.file.FileParser;

import java.io.File;
import java.io.IOException;

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
        getListsFromRegistries();
        createListFiles();
        FileParser.saveRecipesToFile(recipesFile, recipeList.values());
        FileParser.saveToFile(ingredientsFile, itemList.keySet());
        FileParser.saveToFile(aspectsFile, aspectList.keySet());
        FileParser.saveToFile(researchFile, researchList.keySet());
    }

    /**
     * Create any missing lst {@link File}
     */
    private static void createListFiles() throws IOException {
        ingredientsFile = new File(recipeDirectory, "ingredients.lst");
        if (!ingredientsFile.exists())
            if (!ingredientsFile.createNewFile()) throw new NullPointerException("Error Creating Ingredients File");

        aspectsFile = new File(recipeDirectory, "aspects.lst");
        if (!aspectsFile.exists())
            if (!aspectsFile.createNewFile()) throw new NullPointerException("Error Creating Aspect File");

        researchFile = new File(recipeDirectory, "research.lst");
        if (!researchFile.exists())
            if (!researchFile.createNewFile()) throw new NullPointerException("Error Creating Research File");

        recipesFile = new File(recipeDirectory, "recipes.lst");
        if (!recipesFile.exists())
            if (!recipesFile.createNewFile()) throw new NullPointerException("Error Creating Recipes File");

    }
}
