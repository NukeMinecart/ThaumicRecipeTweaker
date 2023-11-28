package nukeminecart.thaumicrecipe.recipes.file;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.*;

/**
 * A class used for all reading of files and changing of a {@link String} to a {@link Recipe}
 */
public class FileParser {

    /**
     * Locates a file from the thaumicrecipe folder and returns the corresponding {@link File}
     *
     * @param name the name of the recipe file
     * @return a file with the
     */
    public static File getFolderFile(String name) {
        return new File(recipeDirectory + name);
    }

    /**
     * Reads {@link File} and returns the lines as a list of strings
     *
     * @param file the file to read
     * @return a list of the lines in the file
     * @throws IOException if the file cannot be read, found, and if an i/o exception occurs
     */
    public static List<String> readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            lines.add(line);
        }

        reader.close();
        return lines;
    }

    /**
     * Parses a {@link List} file
     *
     * @param file the file to parse to a list
     * @return a {@link HashMap} of the file contents separated
     */
    public static HashMap<String, String> parseList(File file) throws IOException {
        HashMap<String, String> list = new HashMap<>();
        List<String> lines = readFile(file);
        for (String line : lines) {
            String[] splitLine = line.split(stringArraySeparator);
            if (splitLine.length == 2) {
                list.put(splitLine[0], splitLine[1]);
            }
        }
        return list;
    }

    /**
     * Returns if a {@link File} exists
     *
     * @param file the file to check
     * @return if the file exists
     */
    public static boolean checkExists(File file) {
        return file.exists() && file.isFile();
    }

    /**
     * Takes a recipe and compresses it into a single line
     * to be saved in a file
     *
     * @param recipe a recipe to compress
     * @return String containing the recipe information
     */
    public static String compressRecipe(Recipe recipe) {
        StringBuilder returnRecipe;
        returnRecipe = new StringBuilder(recipe.getName() + stringSeparator);
        returnRecipe.append(recipe.getType()).append(stringSeparator);
        returnRecipe.append(recipe.getResearch()).append(stringSeparator);
        returnRecipe.append(recipe.getModid()).append(stringSeparator);
        returnRecipe.append(recipe.getInput()).append(stringSeparator);

        for (String ingredient : recipe.getIngredients()) {
            returnRecipe.append(ingredient).append(stringArraySeparator);
        }
        returnRecipe.append(stringSeparator);
        returnRecipe.append(recipe.getOutput()).append(stringSeparator);

        returnRecipe.append(recipe.getVis()).append(stringSeparator);
        for (String aspect : recipe.getAspects()) {
            returnRecipe.append(aspect).append(stringArraySeparator);
        }
        returnRecipe.append(stringSeparator);
        for (Object shape : recipe.getShape()) {
            returnRecipe.append(shape).append(stringArraySeparator);
        }
        return returnRecipe.toString();
    }

    /**
     * Changes custom string recipe to a {@link Recipe}
     *
     * @param line the line to parse
     * @return a recipe class
     * @throws ArrayIndexOutOfBoundsException if the line is missing necessary recipe information
     */
    public static Recipe parseRecipe(String line) {
        String[] compressedRecipe = line.split(stringSeparator);
        String name = compressedRecipe[0];
        String type = compressedRecipe[1];
        String research = compressedRecipe[2];
        String modid = compressedRecipe[3];
        String input = compressedRecipe[4];
        String[] ingredients = compressedRecipe[5].split(stringArraySeparator);
        String output = compressedRecipe[6];
        int vis = Integer.parseInt(compressedRecipe[7]);
        String[] aspects = compressedRecipe[8].split(stringArraySeparator);
        String[] shape = compressedRecipe[9].split(stringArraySeparator);

        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, shape);
    }

    /**
     * Returns all the recipes from a list of strings
     *
     * @param contents the list to get recipes from
     * @return an array of recipes
     */
    public static Recipe[] getRecipesFromString(List<String> contents) {
        List<Recipe> recipes = new ArrayList<>();
        for (String line : contents) {
            recipes.add(parseRecipe(line));
        }
        return recipes.toArray(new Recipe[0]);
    }

    /**
     * Writes to the file with each line corresponding to an index in the contents array
     *
     * @param savefile the file to save to
     * @param contents the contents of the file
     * @throws IOException if the file cannot be written to, doesn't exist, and if an i/o error occurs
     */
    public static void saveToFile(File savefile, List<String> contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(savefile));

        for (String line : contents) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }

    /**
     * Saves a {@link List} of {@link Recipe} to a {@link File} after compressing them
     *
     * @param recipes      the {@link List} of {@link Recipe} to save
     * @param saveLocation the location to save the {@link Recipe} {@link File}
     * @throws IOException if an i/o error occurs
     */
    public static void saveRecipesToFile(File saveLocation, List<Recipe> recipes) throws IOException {
        List<String> compressedRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            compressedRecipes.add(compressRecipe(recipe));
        }
        saveToFile(saveLocation, compressedRecipes);
    }

    /**
     * Sets the current ThaumicRecipeTweakerGUI config file to the selected options
     *
     * @param activeRecipe set the active recipe
     * @param openGUI      if the GUI should open the next time the game launches
     * @throws IOException if the file cannot be written to, read, doesn't exist, and if an i/o error occurs
     */
    public static void setConfigOptions(String activeRecipe, boolean openGUI) throws IOException {
        File configFile = new File(recipeDirectory, "recipe.cfg");
        List<String> contents = new ArrayList<>();
        if (checkExists(configFile)) {
            contents = readFile(configFile);
        } else {
            if (!configFile.createNewFile()) {
                return;
            }
        }
        if (contents.isEmpty()) {
            contents.add(activeRecipe.isEmpty() ? null : ("active-recipe:" + activeRecipe));
        } else {
            String currentActiveRecipe = contents.get(0).replace("active-recipe:", "");
            contents.clear();
            contents.add("active-recipe:" + (activeRecipe.isEmpty() ? currentActiveRecipe : activeRecipe));
        }
        contents.add("open-gui:" + openGUI);
        saveToFile(configFile, contents);
    }
}
