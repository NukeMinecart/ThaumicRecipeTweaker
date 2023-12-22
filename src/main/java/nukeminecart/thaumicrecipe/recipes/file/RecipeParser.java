package nukeminecart.thaumicrecipe.recipes.file;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nukeminecart.thaumicrecipe.loader.RecipeLoader;
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

    /**
     * Unparse a {@link CraftingHelper.ShapedPrimer}
     *
     * @param shapedPrimer the {@link CraftingHelper.ShapedPrimer} to unparse
     * @return the {@link Object} containing the recipe
     */

    public static Object[] unparseShaped(CraftingHelper.ShapedPrimer shapedPrimer) {
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
            if (objectShape[i] != null)
                builder.append(objectShape[i].equals(true) ? "" : objectShape[i]);
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
     * Object array from a string array. Assumes boolean values are represented as empty strings
     * and uses the provided separator to identify nulls and delimit elements.
     *
     * @param recipe Array of strings, the output of convertShapedToRecipe.
     * @return Reconstructed Object array, with booleans as 'true' for empty strings and nulls where
     *         the separator is found. Other elements are returned as strings.
     */
    public static Object[] recipeToShape(String[] recipe) {
        List<Object> reconstructedShape = new ArrayList<>(Arrays.asList(recipe));
        for(String item : recipe){
            reconstructedShape.add(item);
            reconstructedShape.add(itemList.get(item));
        }
        return reconstructedShape.toArray(new Object[0]);
    }
    /**
     * Compile the shape of a recipe
     *
     * @param height the height of the recipe
     * @param width  the width of the recipe
     * @param input  the ingredients of the recipe
     * @return the compiled shape as a {@link String} array
     */

    public static Object[] compileShape(int height, int width, NonNullList<Ingredient> input) {
        CraftingHelper.ShapedPrimer shapedPrimer = new CraftingHelper.ShapedPrimer();
        shapedPrimer.height = height;
        shapedPrimer.width = width;
        shapedPrimer.input = input;
        return RecipeParser.unparseShaped(shapedPrimer);
    }

    /**
     * Load all the recipes in the .rcp {@link File} to the registries
     * @throws IOException if {@link File} errors occur
     */
    public static void loadRecipeToRegistries() throws IOException {
        Recipe[] recipes = FileParser.getRecipesFromString(FileParser.readFile(new File(recipeDirectory, loadedRecipeFile)));
        if(recipes.length == 0) return;
        HashMap<String, Object> forgeRecipes = new HashMap<>();
        for(Recipe recipe : recipes){
            if(recipe.getType().equals("arcane") && recipe.getShape().length > 0)
                forgeRecipes.put(recipe.getName(), recipeToShapedArcane(recipe));

            if(recipe.getType().equals("arcane") && (recipe.getShape().length == 0 || recipe.getShape()==null))
                forgeRecipes.put(recipe.getName(), recipeToShapelessArcane(recipe));

            if(recipe.getType().equals("normal") && recipe.getShape().length > 0)
                forgeRecipes.put(recipe.getName(), recipeToShapedOre(recipe));

            if(recipe.getType().equals("normal")  && (recipe.getShape().length == 0 || recipe.getShape()==null))
                forgeRecipes.put(recipe.getName(), recipeToShapelessOre(recipe));

            if(recipe.getType().equals("crucible"))
                forgeRecipes.put(recipe.getName(), recipeToCrucible(recipe));

            if(recipe.getType().equals("infusion"))
                forgeRecipes.put(recipe.getName(), recipeToInfusion(recipe));

        }
        RecipeLoader.sendRecipesToRegistries(forgeRecipes);
    }

    /**
     * Convert a {@link Recipe} to a {@link ShapedArcaneRecipe}
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapedArcaneRecipe}
     */
    private static ShapedArcaneRecipe recipeToShapedArcane(Recipe recipe){
        Object[] shape = recipeToShape(recipe.getShape());
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        return new ShapedArcaneRecipe(
                new ResourceLocation(""),
                researchList.get(recipe.getResearch()).getKey(),
                recipe.getVis(),
                compileAspects(recipe.getAspects()),
                output,
                shape
        );
    }
    /**
     * Convert a {@link Recipe} to a {@link ShapelessArcaneRecipe}
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapelessArcaneRecipe}
     */
    private static ShapelessArcaneRecipe recipeToShapelessArcane(Recipe recipe){
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        return new ShapelessArcaneRecipe(
                new ResourceLocation(""),
                researchList.get(recipe.getResearch()).getKey(),
                recipe.getVis(),
                compileAspects(recipe.getAspects()),
                output,
                compileIngredients(recipe.getIngredients()));
    }
    /**
     * Convert a {@link Recipe} to a {@link ShapedOreRecipe}
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapedOreRecipe}
     */
    private static ShapedOreRecipe recipeToShapedOre(Recipe recipe){
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        return new ShapedOreRecipe(
                new ResourceLocation(""),
                output,
                recipeToShape(recipe.getShape()));
    }
    /**
     * Convert a {@link Recipe} to a {@link ShapelessOreRecipe}
     * @param recipe the {@link Recipe} to convert
     * @return the {@link ShapelessOreRecipe}
     */
    private static ShapelessOreRecipe recipeToShapelessOre(Recipe recipe){
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        return new ShapelessOreRecipe(
                new ResourceLocation(""),
                output,
                compileIngredients(recipe.getIngredients()));
    }
    /**
     * Convert a {@link Recipe} to a {@link CrucibleRecipe}
     * @param recipe the {@link Recipe} to convert
     * @return the {@link CrucibleRecipe}
     */
    private static CrucibleRecipe recipeToCrucible(Recipe recipe){
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        ItemStack input = itemList.get(recipe.getInput().split(mapSeparator)[0]).getMatchingStacks()[0];
        input.setCount(Integer.parseInt(recipe.getInput().split(mapSeparator)[1]));
        return new CrucibleRecipe(
                researchList.get(recipe.getResearch()).getKey(),
                output,
                input,
                compileAspects(recipe.getAspects()));
    }
    /**
     * Convert a {@link Recipe} to a {@link InfusionRecipe}
     * @param recipe the {@link Recipe} to convert
     * @return the {@link InfusionRecipe}
     */
    private static InfusionRecipe recipeToInfusion(Recipe recipe){
        ItemStack output = itemList.get(recipe.getOutput().split(mapSeparator)[0]).getMatchingStacks()[0];
        output.setCount(Integer.parseInt(recipe.getOutput().split(mapSeparator)[1]));
        ItemStack input = itemList.get(recipe.getInput().split(mapSeparator)[0]).getMatchingStacks()[0];
        input.setCount(Integer.parseInt(recipe.getInput().split(mapSeparator)[1]));
        return new InfusionRecipe(
                researchList.get(recipe.getResearch()).getKey(),
                output,
                recipe.getVis(),
                compileAspects(recipe.getAspects()),
                input,
                compileIngredients(recipe.getIngredients()));
    }

    /**
     * Compile the {@link Aspect} from a {@link HashMap} of their name and amount
     * @param aspects the {@link HashMap} aspects
     * @return the {@link AspectList} aspects
     */
    private static AspectList compileAspects(HashMap<String, Integer> aspects){
        AspectList returnAspectList = new AspectList();
        for(String aspect : aspects.keySet()){
            returnAspectList.add(aspectList.get(aspect), aspects.get(aspect));
        }
        return returnAspectList;
    }
    /**
     * Compile the {@link Ingredient} from a {@link HashMap} of their name and amount
     * @param ingredients the {@link HashMap} ingredients
     * @return the {@link Object} array of the ingredients
     */
    private static Object[] compileIngredients(HashMap<String, Integer> ingredients){
        List<Object> shape = new ArrayList<>();
        for(String ingredient : ingredients.keySet())
            for(int i = 0; i<ingredients.get(ingredient);i++)
                shape.add(itemList.get(ingredient));

        return shape.toArray(new Object[0]);
    }
}
