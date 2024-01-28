package nukeminecart.thaumicrecipe.recipes.file;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nukeminecart.thaumicrecipe.loader.RecipeLoader;
import nukeminecart.thaumicrecipe.recipes.api.ShapedWrapper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static nukeminecart.thaumicrecipe.Config.loadedRecipeFile;
import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.*;
import static nukeminecart.thaumicrecipe.gui.lists.ListRetriever.*;

public class RecipeParser {
    private static final HashMap<String, Character> keyMap = new HashMap<>();
    private static char currentChar = 'a';

    /**
     * Unparse a {@link CraftingHelper.ShapedPrimer}
     *
     * @param shapedPrimer the {@link CraftingHelper.ShapedPrimer} to unparse
     * @return the {@link Object} containing the recipe
     */

    public static Object[] convertShaped(CraftingHelper.ShapedPrimer shapedPrimer) {
        List<Object> recipeComponents = new ArrayList<>();
        HashMap<Ingredient, String> ingredientMap = new HashMap<>();

        for (Ingredient ingredient : shapedPrimer.input) {
            if (ingredient.getMatchingStacks().length > 0) {
                ingredientMap.put(ingredient, ingredient.getMatchingStacks()[0].getDisplayName() + stringArraySeparator);
                reverseIngredientMap.put(ingredient.getMatchingStacks()[0].getDisplayName(), ingredient);
            }
        }

        // Add mirrored flag
        if (shapedPrimer.mirrored) {
            recipeComponents.add(Boolean.TRUE);
        }

        // Reconstruct shape
        StringBuilder[] rows = new StringBuilder[shapedPrimer.height];
        for (int i = 0; i < shapedPrimer.height; i++) {
            rows[i] = new StringBuilder();
        }
        for (int i = 0; i < shapedPrimer.input.size(); i++) {
            Ingredient ingredient = shapedPrimer.input.get(i);
            rows[i / shapedPrimer.width].append(ingredientMap.get(ingredient));
        }

        // Add rows to recipe
        for (StringBuilder row : rows) {
            recipeComponents.add(row.toString());
        }

        // Add ingredients to characters
        for (Map.Entry<Ingredient, String> entry : ingredientMap.entrySet()) {
            recipeComponents.add(entry.getValue());
            recipeComponents.add(entry.getKey());
        }
        return recipeComponents.toArray();
    }

    /**
     * Convert the {@link CraftingHelper.ShapedPrimer} forge format to my {@link Recipe} format
     *
     * @param objectShape the {@link CraftingHelper.ShapedPrimer} format
     * @return the {@link Recipe} format
     */
    public static String[] convertShapedToRecipe(Object... objectShape) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objectShape.length; i++) {
            if (objectShape[i + 1].toString().toLowerCase().contains("ingredient")) {
                break;
            }
            if (objectShape[i] != null) builder.append(objectShape[i].equals(true) ? "" : objectShape[i]);
            else builder.append(stringArraySeparator);
        }
        List<String> returnShape = new ArrayList<>();
        for (String item : builder.toString().split(stringArraySeparator)) {
            returnShape.addAll(Arrays.asList(item.split(stringArraySeparator)));
        }
        return returnShape.toArray(new String[0]);
    }

    /**
     * Reverses the conversion done by convertShapedToRecipe, reconstructing the original
     * Object array from a string array.
     *
     * @param recipe Array of strings, the output of convertShapedToRecipe.
     * @return Reconstructed Object array,
     */
    public static Object[] recipeToShape(String[] recipe) {

        boolean small = isEmpty(recipe, 2) && isEmpty(recipe, 5) && isEmpty(recipe, 6) && isEmpty(recipe, 7) && isEmpty(recipe, 8);
        String[] constructedGroup = addValueGroups(recipe, small);
        Object[] constructedKeys = addKeyPairs(recipe);

        List<Object> reconstructedShape = new ArrayList<>();
        reconstructedShape.add(constructedGroup);
        reconstructedShape.addAll(Arrays.asList(constructedKeys));

        return reconstructedShape.toArray(new Object[0]);
    }

    /**
     * Checks if an array index is empty (null or empty string)
     */
    public static boolean isEmpty(String[] array, int index){
        return array.length <= index || array[index].isEmpty();
    }

    /**
     * Add the 2-3 value groups for the shaped recipes Ex "xyz", "yyy"
     * @param shape the string array representing the shape
     * @param small if the recipe is 2x2 or not
     * @return the modified string array to have the groups of items
     */
    private static String[] addValueGroups(String[] shape, boolean small) {
        List<String> group = new ArrayList<>();
        if(small){
            group.add(getStringArrayRange(shape, 0, 1));
            group.add(getStringArrayRange(shape, 3, 4));
        }else{
            group.add(getStringArrayRange(shape, 0, 2));
            if(shape.length > 3)
                group.add(getStringArrayRange(shape, 3, 5));

            if(shape.length > 6)
                group.add(getStringArrayRange(shape, 6, 8));
        }
        return group.toArray(new String[0]);
    }

    /**
     * Add the key pairs of a shaped recipe
     * @param shape the shape of the recipe
     * @return the object array containing the keys of the shape
     */
    private static Object[] addKeyPairs(String[] shape){
        List<Object> returnList = new ArrayList<>();
        List<String> uniqueList = new ArrayList<>();
        for(String item : shape){
            if(item == null || uniqueList.contains(item) || item.isEmpty()) continue;
            uniqueList.add(item);
            returnList.add(keyMap.get(item));
            if(itemList.containsKey(item))
                returnList.add(itemList.get(item));
            else
                returnList.add(item);
        }
        return returnList.toArray(new Object[0]);
    }

    /**
     * Gets the values in between a string array's startIndex and endIndex
     * @param array the array to get from
     * @param startIndex the start index of the array to start get values from
     * @param endIndex the ending index of the array to get values from
     * @return the values at the range in the array
     */
    private static String getStringArrayRange(String[] array, int startIndex, int endIndex){
        StringBuilder value = new StringBuilder();
        for(;startIndex <= endIndex; startIndex++) value.append(getStringArrayValue(array, startIndex));
        return value.toString();
    }

    /**
     * Get the value of the array at the index, returning an empty string if the element does not exist
     * @param array the array to get from
     * @param index the index to get from the array
     * @return either a string with a space or the value of the array at the string
     */
    private static char getStringArrayValue(String[] array, int index){
        if(array.length <= index || array[index] == null || array[index].isEmpty())
            return ' ';
        else if (!keyMap.containsKey(array[index])){
            keyMap.put(array[index], currentChar);
            return currentChar++;
        }else
            return keyMap.get(array[index]);

    }

    /**
     * Compile the shape of a recipe
     *
     * @param height the height of the recipe
     * @param width  the width of the recipe
     * @param input  the ingredients of the recipe
     * @return the compiled shape as a {@link String} array
     */

    public static Object[] convertShaped(int height, int width, NonNullList<Ingredient> input) {
        CraftingHelper.ShapedPrimer shapedPrimer = new CraftingHelper.ShapedPrimer();
        shapedPrimer.height = height;
        shapedPrimer.width = width;
        shapedPrimer.input = input;
        return RecipeParser.convertShaped(shapedPrimer);
    }

    /**
     * Load all the recipes in the .rcp {@link File} to the registries
     *
     * @throws IOException if {@link File} errors occur
     */
    public static void loadRecipeToRegistries() throws IOException {
        Recipe[] recipes = FileParser.getRecipesFromString(FileParser.readFile(new File(recipeDirectory, loadedRecipeFile)));
        if (recipes.length == 0) return;
        HashMap<String, Object> forgeRecipes = new HashMap<>();
        for (Recipe recipe : recipes) {
            if (recipe.getType().equals("arcane") && (recipe.getShape().length == 0 || recipe.getShape() == null || (recipe.getShape()[0].isEmpty() && recipe.getShape().length == 1)))
                forgeRecipes.put(recipe.getName(), recipeToShapelessArcane(recipe));

            else if (recipe.getType().equals("arcane") && recipe.getShape().length > 0)
                forgeRecipes.put(recipe.getName(), recipeToShapedArcane(recipe));

            else if (recipe.getType().equals("normal") && (recipe.getShape().length == 0 || recipe.getShape() == null || (recipe.getShape()[0].isEmpty() && recipe.getShape().length == 1)))
                forgeRecipes.put(recipe.getName(), recipeToShapelessOre(recipe));

            else if (recipe.getType().equals("normal") && recipe.getShape().length > 0)
                forgeRecipes.put(recipe.getName(), recipeToShapedOre(recipe));

            else if (recipe.getType().equals("crucible")) forgeRecipes.put(recipe.getName(), recipeToCrucible(recipe));

            else if (recipe.getType().equals("infusion")) forgeRecipes.put(recipe.getName(), recipeToInfusion(recipe));

        }
        RecipeLoader.sendRecipesToRegistries(forgeRecipes);
    }

    /**
     * Convert a {@link Recipe} to a {@link ShapedArcaneRecipe}
     *
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapedArcaneRecipe}
     */
    private static ShapedArcaneRecipe recipeToShapedArcane(Recipe recipe) {
        Object[] shape = recipeToShape(recipe.getShape());
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        return new ShapedArcaneRecipe(new ResourceLocation(""), researchList.get(recipe.getResearch()).getKey(), recipe.getVis(), compileAspects(recipe.getAspects()), output, shape);
    }

    /**
     * Convert a {@link Recipe} to a {@link ShapelessArcaneRecipe}
     *
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapelessArcaneRecipe}
     */
    private static ShapelessArcaneRecipe recipeToShapelessArcane(Recipe recipe) {
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        return new ShapelessArcaneRecipe(new ResourceLocation(""), researchList.get(recipe.getResearch()).getKey(), recipe.getVis(), compileAspects(recipe.getAspects()), output, compileIngredients(recipe.getIngredients()));
    }

    /**
     * Convert a {@link Recipe} to a {@link ShapedOreRecipe}
     *
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapedOreRecipe}
     */
    private static ShapedWrapper recipeToShapedOre(Recipe recipe) {
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        Object[] shape = recipeToShape(recipe.getShape());
        return new ShapedWrapper(new ShapedOreRecipe(new ResourceLocation(""), output, shape) ,shape);
    }

    /**
     * Convert a {@link Recipe} to a {@link ShapelessOreRecipe}
     *
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapelessOreRecipe}
     */
    private static ShapelessOreRecipe recipeToShapelessOre(Recipe recipe) {
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        return new ShapelessOreRecipe(new ResourceLocation(""), output, compileIngredients(recipe.getIngredients()));
    }

    /**
     * Convert a {@link Recipe} to a {@link CrucibleRecipe}
     *
     * @param recipe the {@link Recipe} to convert
     * @return the {@link CrucibleRecipe}
     */
    private static CrucibleRecipe recipeToCrucible(Recipe recipe) {
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        ItemStack input = itemList.get(recipe.getInput().split(mapSeparator)[0]).getMatchingStacks()[0];
        input.setCount(Integer.parseInt(recipe.getInput().split(mapSeparator)[1]));
        return new CrucibleRecipe(researchList.get(recipe.getResearch()).getKey(), output, input, compileAspects(recipe.getAspects()));
    }

    /**
     * Convert a {@link Recipe} to a {@link InfusionRecipe}
     *
     * @param recipe the {@link Recipe} to convert
     * @return the {@link InfusionRecipe}
     */
    private static InfusionRecipe recipeToInfusion(Recipe recipe) {
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        ItemStack input = itemList.get(recipe.getInput().split(mapSeparator)[0]).getMatchingStacks()[0];
        input.setCount(Integer.parseInt(recipe.getInput().split(mapSeparator)[1]));
        return new InfusionRecipe(researchList.get(recipe.getResearch()).getKey(), output, recipe.getVis(), compileAspects(recipe.getAspects()), input, compileIngredients(recipe.getIngredients()));
    }

    /**
     * Compile the {@link Aspect} from a {@link HashMap} of their name and amount
     *
     * @param aspects the {@link HashMap} aspects
     * @return the {@link AspectList} aspects
     */
    private static AspectList compileAspects(HashMap<String, Integer> aspects) {
        AspectList returnAspectList = new AspectList();
        for (String aspect : aspects.keySet()) {
            returnAspectList.add(aspectList.get(aspect), aspects.get(aspect));
        }
        return returnAspectList;
    }

    /**
     * Compile the {@link Ingredient} from a {@link HashMap} of their name and amount
     *
     * @param ingredients the {@link HashMap} ingredients
     * @return the {@link Object} array of the ingredients
     */
    private static Object[] compileIngredients(HashMap<String, Integer> ingredients) {
        List<Object> shape = new ArrayList<>();
        for (String ingredient : ingredients.keySet())
            for (int i = 0; i < ingredients.get(ingredient); i++)
                shape.add(itemList.get(ingredient));

        return shape.toArray(new Object[0]);
    }
}
