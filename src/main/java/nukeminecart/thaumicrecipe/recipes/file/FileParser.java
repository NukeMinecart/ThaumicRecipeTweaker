package nukeminecart.thaumicrecipe.recipes.file;


import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.*;

/**
 * A class used for all reading of files and changing of a {@link String} to a {@link Recipe}
 */
public class FileParser {


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
     * Split a {@link String} according to the splitString
     *
     * @param string      the {@link String to split}
     * @param splitString the {@link String} to split with
     * @return a {@link String} array
     */
    public static String[] splitString(String string, String splitString) {
        List<String> strings = new ArrayList<>();
        for (String endString = string; string.contains(splitString); ) {
            if (endString.contains(splitString)) strings.add(endString.substring(0, endString.indexOf(splitString)));
            else {
                strings.add(endString);
                break;
            }
            endString = endString.substring(endString.indexOf(splitString) + splitString.length());
        }
        return strings.toArray(new String[0]);
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

        for (String ingredient : recipe.getIngredients().keySet()) {
            returnRecipe.append(ingredient).append(mapSeparator).append(recipe.getIngredients().get(ingredient)).append(stringArraySeparator);
        }
        returnRecipe.append(stringSeparator);
        returnRecipe.append(recipe.getOutput()).append(stringSeparator);

        returnRecipe.append(recipe.getVis()).append(stringSeparator);
        for (String aspect : recipe.getAspects().keySet()) {
            returnRecipe.append(aspect).append(mapSeparator).append(recipe.getAspects().get(aspect)).append(stringArraySeparator);
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
        String[] compressedRecipe = splitString(line, stringSeparator);
        String name = null;
        String type = null;
        String research = null;
        String modid = null;
        String input = null;
        HashMap<String, Integer> ingredients = new HashMap<>();
        String output = null;
        int vis = 0;
        HashMap<String, Integer> aspects = new HashMap<>();
        String[] shape = null;
        try {
            name = compressedRecipe[0];
            type = compressedRecipe[1];
            research = compressedRecipe[2];
            modid = compressedRecipe[3];
            input = compressedRecipe[4];
            ingredients = new HashMap<>();
            for (String ingredient : compressedRecipe[5].split(stringArraySeparator)) {
                ingredients.put(ingredient.split(mapSeparator)[0], Integer.parseInt(ingredient.split(mapSeparator)[1]));
            }
            output = compressedRecipe[6];
            vis = Integer.parseInt(compressedRecipe[7]);
            aspects = new HashMap<>();
            for (String aspect : compressedRecipe[8].split(stringArraySeparator)) {
                aspects.put(aspect.split(mapSeparator)[0], Integer.parseInt(aspect.split(mapSeparator)[1]));
            }
            shape = compressedRecipe[9].split(stringArraySeparator);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
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
     * @param original if the saving is a needs to stay as it is
     * @throws IOException if the file cannot be written to, doesn't exist, and if an i/o error occurs
     */
    public static void saveToFile(File savefile, Collection<String> contents, boolean original) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(savefile));
        for (int i = 0; i < contents.size(); i++) {
            String line = contents.toArray(new String[0])[i];
            if (!original)
                line = line.replace(mapSeparator, " ").replace(stringSeparator, " ").replace(stringArraySeparator, " ");
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
    public static void saveRecipesToFile(File saveLocation, HashMap<String, Recipe> recipes) throws IOException {
        List<String> compressedRecipes = new ArrayList<>();
        for (Recipe recipe : recipes.values()) {
            compressedRecipes.add(compressRecipe(recipe));
        }
        saveToFile(saveLocation, compressedRecipes, true);
    }

    /**
     * Load the config options from the thaumicrecipetweakerGUI config file
     *
     * @return the config options in a {@link Boolean} array
     */
    public static HashMap<String, String> loadConfigOptions() throws IOException {
        List<String> contents = readFile(new File(recipeDirectory, "recipe.cfg"));
        HashMap<String, String> returnMap = new HashMap<>();
        for (String item : contents)
            returnMap.put(item.split(mapSeparator)[0], item.split(mapSeparator)[1]);
        return returnMap;
    }
}
