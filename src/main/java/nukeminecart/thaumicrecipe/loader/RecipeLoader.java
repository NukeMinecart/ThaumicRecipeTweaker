package nukeminecart.thaumicrecipe.loader;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nukeminecart.thaumicrecipe.recipes.api.ShapedWrapper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.crafting.*;

import java.util.HashMap;

public class RecipeLoader {

    /**
     * Sends an {@link IRecipe} to the place that it needs to go based on what {@link Class} it is
     *
     * @param recipe the {@link IRecipe} to send
     */
    private static void sendIRecipe(IRecipe recipe) {
        if (recipe instanceof ShapedArcaneRecipe || recipe instanceof ShapelessArcaneRecipe)
            ThaumcraftApi.addArcaneCraftingRecipe(recipe.getRecipeOutput().getItem().getRegistryName(), (IArcaneRecipe) recipe);
        if (recipe instanceof ShapelessOreRecipe) sendShapelessOreRecipe((ShapelessOreRecipe) recipe);
    }

    /**
     * Sends a {@link ShapedOreRecipe} to the forge {@link GameRegistry}
     *
     * @param recipe the {@link ShapedOreRecipe} to send
     */
    private static void sendShapedOreRecipe(ShapedWrapper recipe) {
        GameRegistry.addShapedRecipe(recipe.getRecipe().getRecipeOutput().getItem().getRegistryName(), null, recipe.getRecipe().getRecipeOutput(), recipe.getShape());
    }

    /**
     * Sends a {@link ShapelessOreRecipe} to the forge {@link GameRegistry}
     *
     * @param recipe the {@link ShapelessOreRecipe} to send
     */
    private static void sendShapelessOreRecipe(ShapelessOreRecipe recipe) {
        GameRegistry.addShapelessRecipe(recipe.getRecipeOutput().getItem().getRegistryName(), null, recipe.getRecipeOutput(), recipe.getIngredients().toArray(new Ingredient[0]));
    }

    /**
     * Sends an {@link IThaumcraftRecipe} to the {@link ThaumcraftApi}
     *
     * @param recipe the {@link IThaumcraftRecipe} to send
     */
    private static void sendThaumcraftRecipe(IThaumcraftRecipe recipe) {
        if (recipe instanceof CrucibleRecipe)
            ThaumcraftApi.addCrucibleRecipe(((CrucibleRecipe) recipe).getRecipeOutput().getItem().getRegistryName(), (CrucibleRecipe) recipe);
        if (recipe instanceof InfusionRecipe)
            ThaumcraftApi.addInfusionCraftingRecipe(ThaumcraftApiHelper.getIngredient(((InfusionRecipe) recipe).getRecipeOutput()).getMatchingStacks()[0].getItem().getRegistryName(), (InfusionRecipe) recipe);
    }

    /**
     * Sends a {@link HashMap} of recipes to their correct registry
     *
     * @param recipes the recipes to send
     */
    public static void sendRecipesToRegistries(HashMap<String, Object> recipes) {
        for (Object recipe : recipes.values()) {
            if (recipe instanceof IRecipe) sendIRecipe((IRecipe) recipe);
            if (recipe instanceof IThaumcraftRecipe) sendThaumcraftRecipe((IThaumcraftRecipe) recipe);
            if (recipe instanceof ShapedWrapper) sendShapedOreRecipe((ShapedWrapper) recipe);
        }
    }
}
