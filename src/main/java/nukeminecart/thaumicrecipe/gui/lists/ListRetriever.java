package nukeminecart.thaumicrecipe.gui.lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nukeminecart.thaumicrecipe.recipes.file.Recipe;
import nukeminecart.thaumicrecipe.recipes.file.RecipeParser;
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
            itemList.put(Objects.requireNonNull(item.getRegistryName()).toString(), item);
        }
    }

    /**
     * Get all the {@link ResearchEntry} from all the {@link ResearchCategories}
     */
    private static void getResearchList() {
        //TODO -> Look at research.lst and fix that error
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
        parseIRecipes();
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
            else if (irecipe instanceof ShapedOreRecipe) type = "ShapedOre";
            else if (irecipe instanceof ShapelessOreRecipe) type = "ShapelessOre";
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
                case "ShapedOre":
                    returnRecipe = convertShapedOre((ShapedOreRecipe) irecipe);
                    break;
                case "ShapelessOre":
                    returnRecipe = convertShapelessOre((ShapelessOreRecipe) irecipe);
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
        HashMap<String, Integer> ingredients = new HashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getMatchingStacks().length > 0)
                ingredients.put(ingredient.getMatchingStacks()[0].getDisplayName().replace(" ", "").toLowerCase(), 1);
        }
        String output = recipe.getRecipeOutput().getDisplayName();

        CraftingHelper.ShapedPrimer shapedPrimer = new CraftingHelper.ShapedPrimer();
        shapedPrimer.height = recipe.getRecipeHeight();
        shapedPrimer.width = recipe.getRecipeWidth();
        shapedPrimer.input = recipe.getIngredients();
        Object[] shape = RecipeParser.unparseShaped(shapedPrimer);
        String[] parsedShape = RecipeParser.convertShapedToRecipe(shape);

        HashMap<String, Integer> aspects = new HashMap<>();
        if (recipe.getCrystals() != null) {
            for (Aspect aspect : recipe.getCrystals().getAspects()) {
                aspects.put(aspect.getName(), recipe.getCrystals().getAmount(aspect));
            }
        }
        int vis = recipe.getVis();
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, parsedShape);
    }

    /**
     * Converts a {@link ShapelessArcaneRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link ShapelessArcaneRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertShapelessArcane(ShapelessArcaneRecipe recipe) {
        String name = recipe.getRecipeOutput().getDisplayName();
        String type = "arcane";
        String research = recipe.getResearch();
        String modid = Objects.requireNonNull(recipe.getRegistryName()).getResourceDomain();
        String input = "";
        HashMap<String, Integer> ingredients = new HashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getMatchingStacks().length > 0)
                ingredients.put(ingredient.getMatchingStacks()[0].getDisplayName().replace(" ", "").toLowerCase(), 1);
        }
        String output = recipe.getRecipeOutput().getDisplayName();

        HashMap<String, Integer> aspects = new HashMap<>();
        if (recipe.getCrystals() != null) {
            for (Aspect aspect : recipe.getCrystals().getAspects()) {
                aspects.put(aspect.getName(), recipe.getCrystals().getAmount(aspect));
            }
        }
        int vis = recipe.getVis();
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects);
    }

    /**
     * Converts a {@link CrucibleRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link CrucibleRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertCrucible(CrucibleRecipe recipe) {
        String name = recipe.getRecipeOutput().getDisplayName();
        String type = "crucible";
        String research = recipe.getResearch();
        String modid = recipe.getGroup();
        String input = recipe.getCatalyst().getMatchingStacks()[0].getDisplayName();
        HashMap<String, Integer> ingredients = new HashMap<>();
        String output = recipe.getRecipeOutput().getDisplayName();

        HashMap<String, Integer> aspects = new HashMap<>();
        if (recipe.getAspects() != null) {
            for (Aspect aspect : recipe.getAspects().getAspects()) {
                aspects.put(aspect.getName(), recipe.getAspects().getAmount(aspect));
            }
        }
        return new Recipe(name, type, research, modid, input, ingredients, output, 0, aspects);
    }

    /**
     * Converts a {@link InfusionRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link InfusionRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertInfusion(InfusionRecipe recipe) {
        String name = recipe.getGroup();
        String type = "infusion";
        String research = recipe.getResearch();
        String modid = recipe.getGroup();
        String input = "";
        HashMap<String, Integer> ingredients = new HashMap<>();
        for (Ingredient ingredient : recipe.getComponents()) {
            String key = ingredient.getMatchingStacks()[0].getDisplayName().replace(" ", "").toLowerCase();
            if (ingredients.containsKey(key)) ingredients.put(key, ingredients.get(key) + 1);
            else ingredients.put(key, 1);
        }
        String output = Objects.requireNonNull(((Item) recipe.getRecipeOutput()).getRegistryName()).getResourceDomain();

        HashMap<String, Integer> aspects = new HashMap<>();
        if (recipe.getAspects() != null) {
            for (Aspect aspect : recipe.getAspects().getAspects()) {
                aspects.put(aspect.getName(), recipe.getAspects().getAmount(aspect));
            }
        }
        return new Recipe(name, type, research, modid, input, ingredients, output, 0, aspects);
    }

    /**
     * Converts a {@link ShapedRecipes} to a {@link Recipe}
     *
     * @param recipe the {@link ShapedRecipes}
     * @return the {@link Recipe}
     */
    private static Recipe convertShaped(ShapedRecipes recipe) {
        String name = recipe.getRecipeOutput().getDisplayName();
        String type = "normal";
        String research = "";
        String modid = Objects.requireNonNull(recipe.getRegistryName()).getResourceDomain();
        String input = "";
        HashMap<String, Integer> ingredients = new HashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getMatchingStacks().length > 0)
                ingredients.put(ingredient.getMatchingStacks()[0].getDisplayName().replace(" ", "").toLowerCase(), 1);
        }
        String output = recipe.getRecipeOutput().getDisplayName();

        CraftingHelper.ShapedPrimer shapedPrimer = new CraftingHelper.ShapedPrimer();
        shapedPrimer.height = recipe.getRecipeHeight();
        shapedPrimer.width = recipe.getRecipeWidth();
        shapedPrimer.input = recipe.getIngredients();
        Object[] shape = RecipeParser.unparseShaped(shapedPrimer);
        String[] parsedShape = RecipeParser.convertShapedToRecipe(shape);

        HashMap<String, Integer> aspects = new HashMap<>();
        int vis = 0;
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, parsedShape);
    }

    /**
     * Converts a {@link ShapelessRecipes} to a {@link Recipe}
     *
     * @param recipe the {@link ShapelessRecipes}
     * @return hte {@link Recipe}
     */
    private static Recipe convertShapeless(ShapelessRecipes recipe) {
        String name = recipe.getRecipeOutput().getDisplayName();
        String type = "normal";
        String research = "";
        String modid = Objects.requireNonNull(recipe.getRegistryName()).getResourceDomain();
        String input = "";
        HashMap<String, Integer> ingredients = new HashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getMatchingStacks().length > 0)
                ingredients.put(ingredient.getMatchingStacks()[0].getDisplayName().replace(" ", "").toLowerCase(), 1);
        }
        String output = recipe.getRecipeOutput().getDisplayName();

        HashMap<String, Integer> aspects = new HashMap<>();
        int vis = 0;
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects);
    }

    /**
     * Converts a {@link ShapedOreRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link ShapedOreRecipe}
     * @return hte {@link Recipe}
     */
    private static Recipe convertShapedOre(ShapedOreRecipe recipe) {
        String name = recipe.getRecipeOutput().getDisplayName();
        String type = "normal";
        String research = "";
        String modid = Objects.requireNonNull(recipe.getRegistryName()).getResourceDomain();
        String input = "";
        HashMap<String, Integer> ingredients = new HashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getMatchingStacks().length > 0)
                ingredients.put(ingredient.getMatchingStacks()[0].getDisplayName().replace(" ", "").toLowerCase(), 1);
        }
        String output = recipe.getRecipeOutput().getDisplayName();

        CraftingHelper.ShapedPrimer shapedPrimer = new CraftingHelper.ShapedPrimer();
        shapedPrimer.height = recipe.getRecipeHeight();
        shapedPrimer.width = recipe.getRecipeWidth();
        shapedPrimer.input = recipe.getIngredients();
        Object[] shape = RecipeParser.unparseShaped(shapedPrimer);
        String[] parsedShape = RecipeParser.convertShapedToRecipe(shape);

        HashMap<String, Integer> aspects = new HashMap<>();
        int vis = 0;
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, parsedShape);
    }

    /**
     * Converts a {@link ShapelessOreRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link ShapelessOreRecipe}
     * @return hte {@link Recipe}
     */
    private static Recipe convertShapelessOre(ShapelessOreRecipe recipe) {
        String name = recipe.getRecipeOutput().getDisplayName();
        String type = "normal";
        String research = "";
        String modid = Objects.requireNonNull(recipe.getRegistryName()).getResourceDomain();
        String input = "";
        HashMap<String, Integer> ingredients = new HashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getMatchingStacks().length > 0)
                ingredients.put(ingredient.getMatchingStacks()[0].getDisplayName().replace(" ", "").toLowerCase(), 1);
        }
        String output = recipe.getRecipeOutput().getDisplayName();

        HashMap<String, Integer> aspects = new HashMap<>();
        int vis = 0;
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects);
    }

}
