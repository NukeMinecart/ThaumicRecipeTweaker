package nukeminecart.thaumicrecipe.recipes.file;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.*;

import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.reverseIngredientMap;
import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.stringArraySeparator;

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
                ingredientMap.put(ingredient, ingredient.getMatchingStacks()[0] + stringArraySeparator);
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

    public static String[] convertShapedToRecipe(Object... objectShape) {
        StringBuilder builder = new StringBuilder();
        //TODO REMOVE NULL VALUES
        for (int i = 0; i < objectShape.length; i++) {
            if (objectShape[i + 1].toString().toLowerCase().contains("ingredient")) {
                break;
            }
            if(objectShape[i] != null)
                builder.append(objectShape[i].equals(true) ? "" : objectShape[i]);
            else builder.append(stringArraySeparator);
        }
        List<String> returnShape = new ArrayList<>();
        for (String item : builder.toString().split(stringArraySeparator)) {
            returnShape.addAll(Arrays.asList(item.split(stringArraySeparator)));
        }
        return returnShape.toArray(new String[0]);
    }
}
