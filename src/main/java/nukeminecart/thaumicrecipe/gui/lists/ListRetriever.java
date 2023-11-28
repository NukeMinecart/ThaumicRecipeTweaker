package nukeminecart.thaumicrecipe.gui.lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import nukeminecart.thaumicrecipe.recipes.file.Recipe;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;

import java.util.*;

public class ListRetriever {
    public static HashMap<String, Item> itemList = new HashMap<>();
    public static HashMap<String, ResearchEntry> researchList = new HashMap<>();
    public static HashMap<String, Aspect> aspectList = new HashMap<>();
    public static HashMap<String, IRecipe> irecipeList = new HashMap<>();
    public static HashMap<String, Recipe> recipeList = new HashMap<>();


    /**
     * Get all the {@link List} from {@link ForgeRegistries}
     */
    public static void getListsFromRegistries() {
        getResearchList();
        getItemList();
        getAspectList();
        getRecipeList();
    }

    /**
     * Get all of the {@link Item} and {@link Block} from {@link ForgeRegistries}
     */
    private static void getItemList() {
        for (Item item : ForgeRegistries.ITEMS) {
            itemList.put(item.getUnlocalizedName(), item);

        }
    }

    /**
     * Get all the {@link ResearchEntry} from all the {@link ResearchCategories}
     */
    private static void getResearchList() {
        List<Map<String, ResearchEntry>> researchEntries = new ArrayList<>();
        for (ResearchCategory category : ResearchCategories.researchCategories.values()) {
            researchEntries.add(category.research);
        }
        for (Map<String, ResearchEntry> entry : researchEntries) {
            for (ResearchEntry researchEntry : entry.values()) {
                researchList.put(researchEntry.getName(), researchEntry);
            }
        }
    }

    /**
     * Get all the {@link Aspect} loaded
     */
    private static void getAspectList() {
        aspectList = Aspect.aspects;
    }

    /**
     * Get all the {@link IRecipe} loaded
     */
    private static void getRecipeList() {
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            irecipeList.put(recipe.getRecipeOutput().getDisplayName(), recipe);
        }

    }

    /**
     * Parses all the {@link IRecipe} into {@link Recipe}
     */
    private static void parseIRecipes() {
        for (IRecipe irecipe : irecipeList.values()) {
            String type;
            if (irecipe instanceof ShapedArcaneRecipe) type = "ShapedArcane";
            else if (irecipe instanceof ShapelessArcaneRecipe) type = "ShapelessArcane";
            else if (irecipe instanceof CrucibleRecipe) type = "Crucible";
            else if (irecipe instanceof InfusionRecipe) type = "Infusion";
            else if (irecipe instanceof ShapedRecipes) type = "Shaped";
            else if (irecipe instanceof ShapelessRecipes) type = "Shapeless";
            else continue;
            Recipe returnRecipe;
            switch (type) {
                default:
                    continue;
                case "ShapedArcane":
                    returnRecipe = convertShapedArcane((ShapedArcaneRecipe) irecipe);
                    break;
                case "ShapelessArcane":
                    returnRecipe = convertShapelessArcane((ShapelessArcaneRecipe) irecipe);
                    break;
                case "Crucible":
                    returnRecipe = convertCrucible((CrucibleRecipe) irecipe);
                    break;
                case "Infusion":
                    returnRecipe = convertInfusion((InfusionRecipe) irecipe);
                    break;
                case "Shaped":
                    returnRecipe = convertShaped((ShapedRecipes) irecipe);
                    break;
                case "Shapeless":
                    returnRecipe = convertShapeless((ShapelessRecipes) irecipe);
                    break;

            }
            recipeList.put(returnRecipe.getName(), returnRecipe);
        }
    }

    /**
     * Converts a {@link ShapedArcaneRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link ShapedArcaneRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertShapedArcane(ShapedArcaneRecipe recipe) {
        String name = recipe.getRecipeOutput().getDisplayName();
        String type = "arcane";
        String research = recipe.getResearch();
        String modid = Objects.requireNonNull(recipe.getRegistryName()).getResourceDomain();
        String input = "";
        List<String> ingredients = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients.add(ingredient.getMatchingStacks()[0].getItem().getUnlocalizedName());
        }
        String output = recipe.getRecipeOutput().getDisplayName();

        List<String> aspects = new ArrayList<>();
        for (Aspect aspect : recipe.getCrystals().getAspects()) {
            aspects.add(aspect.getName());
        }
        int vis = recipe.getVis();
        //TODO Finish shape and add counts to the aspects in the GUI
        return new Recipe(name, type, research, modid, input, ingredients.toArray(new String[0]), output, vis, aspects.toArray(new String[0]));
    }

    /**
     * Converts a {@link ShapelessArcaneRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link ShapelessArcaneRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertShapelessArcane(ShapelessArcaneRecipe recipe) {

    }

    /**
     * Converts a {@link CrucibleRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link CrucibleRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertCrucible(CrucibleRecipe recipe) {

    }

    /**
     * Converts a {@link InfusionRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link InfusionRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertInfusion(InfusionRecipe recipe) {

    }

    /**
     * Converts a {@link ShapedRecipes} to a {@link Recipe}
     *
     * @param recipe the {@link ShapedRecipes}
     * @return the {@link Recipe}
     */
    private static Recipe convertShaped(ShapedRecipes recipe) {
    }

    /**
     * Converts a {@link ShapelessRecipes} to a {@link Recipe}
     *
     * @param recipe the {@link ShapelessRecipes}
     * @return hte {@link Recipe}
     */
    private static Recipe convertShapeless(ShapelessRecipes recipe) {

    }
}
