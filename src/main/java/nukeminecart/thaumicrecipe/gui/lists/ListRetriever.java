package nukeminecart.thaumicrecipe.gui.lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nukeminecart.thaumicrecipe.recipes.file.Recipe;
import nukeminecart.thaumicrecipe.recipes.file.RecipeParser;
import org.apache.commons.lang3.StringUtils;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;

import java.util.*;

import static nukeminecart.thaumicrecipe.ThaumicRecipeConstants.MOD_ID;

public class ListRetriever {
    public static HashMap<String, Ingredient> itemList = new HashMap<>();
    public static HashMap<String, ResearchEntry> researchList = new HashMap<>();
    public static HashMap<String, Aspect> aspectList = new HashMap<>();
    public static HashMap<String, Object> irecipeList = new HashMap<>();
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
        for (Item item : ForgeRegistries.ITEMS)
            for(ItemStack matchingStack : ThaumcraftApiHelper.getIngredient(item).getMatchingStacks())
                itemList.put(matchingStack.getDisplayName(),ThaumcraftApiHelper.getIngredient(matchingStack));
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
                researchList.put(researchEntry.getKey(), researchEntry);
            }
        }
    }

    /**
     * Get all the {@link Aspect} loaded
     */
    private static void getAspectList() {
        aspectList = Aspect.aspects;
        aspectList.forEach((s, aspect) -> StringUtils.capitalize(s));
    }

    /**
     * Get all the {@link IRecipe} loaded
     */
    private static void getRecipeList() {
        for (IRecipe recipe : ForgeRegistries.RECIPES)
            irecipeList.put(recipe.getRecipeOutput().getDisplayName(), recipe);

        for (ResourceLocation location : ThaumcraftApi.getCraftingRecipes().keySet())
            irecipeList.put(location.getResourcePath(), ThaumcraftApi.getCraftingRecipes().get(location));

        parseIRecipes();
    }

    /**
     * Parses all the {@link IRecipe} into {@link Recipe}
     */
    private static void parseIRecipes() {
        //TODO CHANGE THE GET INGREDIENTS TO GET THE CORRECT ONES
        for (Object irecipe : irecipeList.values()) {
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
            if(returnRecipe != null)
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
        String modid = getModid(recipe.getRegistryName());
        String input = "";
        HashMap<String, Integer> ingredients = compileIngredients(recipe.getIngredients());
        String output = getNameFromItemStack(recipe.getRecipeOutput());

        String[] shape = compileShape(recipe.getRecipeHeight(), recipe.getRecipeWidth(), recipe.getIngredients());

        HashMap<String, Integer> aspects = compileAspects(recipe.getCrystals());

        int vis = recipe.getVis();
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, shape);
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
        String modid = getModid(recipe.getRegistryName());
        String input = "";
        HashMap<String, Integer> ingredients = compileIngredients(recipe.getIngredients());
        String output = getNameFromItemStack(recipe.getRecipeOutput());

        HashMap<String, Integer> aspects = compileAspects(recipe.getCrystals());

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
        String modid = Objects.requireNonNull(recipe.getRecipeOutput().getItem().getRegistryName()).getResourceDomain();
        String input = getNameFromItemStack(recipe.getCatalyst().getMatchingStacks()[0]);
        HashMap<String, Integer> ingredients = new HashMap<>();
        String output = getNameFromItemStack(recipe.getRecipeOutput());

        HashMap<String, Integer> aspects = compileAspects(recipe.getAspects());

        return new Recipe(name, type, research, modid, input, ingredients, output, 0, aspects);
    }

    /**
     * Converts a {@link InfusionRecipe} to a {@link Recipe}
     *
     * @param recipe the {@link InfusionRecipe}
     * @return the {@link Recipe}
     */
    private static Recipe convertInfusion(InfusionRecipe recipe) {
        String type = "infusion";
        String research = recipe.getResearch();
        String input = null;
        if (recipe.getRecipeInput().getMatchingStacks().length > 0) input = recipe.getRecipeInput().getMatchingStacks()[0].isEmpty() ? null : getNameFromItemStack(ThaumcraftApiHelper.getIngredient(recipe.getRecipeInput()).getMatchingStacks()[0]);
        HashMap<String, Integer> ingredients = compileIngredients(recipe.getComponents());
        Ingredient outputTest = ThaumcraftApiHelper.getIngredient(recipe.getRecipeOutput());
        String output;
        String name;
        String modid;
        if (outputTest != null) {
            if(outputTest.getMatchingStacks().length > 0) {
                output = getNameFromItemStack(outputTest.getMatchingStacks()[0]);
                name = outputTest.getMatchingStacks()[0].getDisplayName();
                modid = getModid(outputTest.getMatchingStacks()[0]);
            }else{
                return null;
            }
        }else{
            output = null;
            name = ingredients.keySet().toArray(new String[0])[0];
            modid = MOD_ID;
        }
        HashMap<String, Integer> aspects = compileAspects(recipe.getAspects());

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
        String modid = getModid(recipe.getRegistryName());
        String input = "";
        HashMap<String, Integer> ingredients = compileIngredients(recipe.getIngredients());
        String output = getNameFromItemStack(recipe.getRecipeOutput());

        String[] shape = compileShape(recipe.getRecipeHeight(), recipe.getRecipeWidth(), recipe.getIngredients());

        HashMap<String, Integer> aspects = new HashMap<>();
        int vis = 0;
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, shape);
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
        String modid = getModid(recipe.getRegistryName());
        String input = "";
        HashMap<String, Integer> ingredients = compileIngredients(recipe.getIngredients());
        String output = getNameFromItemStack(recipe.getRecipeOutput());

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
        String modid = getModid(recipe.getRegistryName());
        String input = "";
        HashMap<String, Integer> ingredients = compileIngredients(recipe.getIngredients());
        String output = recipe.getRecipeOutput().getDisplayName();
        String[] shape = compileShape(recipe.getRecipeHeight(), recipe.getRecipeWidth(), recipe.getIngredients());

        HashMap<String, Integer> aspects = new HashMap<>();
        int vis = 0;
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, shape);
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
        String modid = getModid(recipe.getRegistryName());
        String input = "";
        HashMap<String, Integer> ingredients = compileIngredients(recipe.getIngredients());
        String output = recipe.getRecipeOutput().getDisplayName();

        HashMap<String, Integer> aspects = new HashMap<>();
        int vis = 0;
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects);
    }

    /**
     * Get the modid of an {@link Object} as a {@link String}
     *
     * @param item the {@link Object} to get the modid of
     * @return a {@link String} representing the modid
     */
    private static String getModid(Object item) {
        if (item instanceof Item) return Objects.requireNonNull(((Item) item).getRegistryName()).getResourceDomain();
        else if (item instanceof ResourceLocation)
            return Objects.requireNonNull((ResourceLocation) item).getResourceDomain();
        else if (item instanceof ItemStack)
            return Objects.requireNonNull(((ItemStack) item).getItem().getRegistryName()).getResourceDomain();
        else if (item instanceof Ingredient) {
            return getModid(((Ingredient) item).getMatchingStacks()[0].getItem());
        }
        return null;
    }

    /**
     * Convert a {@link ItemStack} to a {@link String}
     *
     * @param item the {@link ItemStack} to convert
     * @return the {@link String} of the {@link ItemStack}
     */
    private static String getNameFromItemStack(ItemStack item) {
        return item.getDisplayName();
    }

    /**
     * Compile a {@link List} of ingredients together into a {@link HashMap}
     *
     * @param ingredients the ingredients to compile
     * @return a {@link HashMap} containing the ingredients
     */
    private static HashMap<String, Integer> compileIngredients(List<Ingredient> ingredients) {
        HashMap<String, Integer> ingredientsMap = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            for(ItemStack matchingStack : ingredient.getMatchingStacks()){
                String key = getNameFromItemStack(matchingStack);
                if (ingredientsMap.containsKey(key)) ingredientsMap.put(key, ingredientsMap.get(key) + 1);
                else ingredientsMap.put(key, 1);
            }
        }
        return ingredientsMap;
    }

    /**
     * Compile the shape of a recipe
     *
     * @param height the height of the recipe
     * @param width  the width of the recipe
     * @param input  the ingredients of the recipe
     * @return the compiled shape as a {@link String} array
     */

    private static String[] compileShape(int height, int width, NonNullList<Ingredient> input) {
        CraftingHelper.ShapedPrimer shapedPrimer = new CraftingHelper.ShapedPrimer();
        shapedPrimer.height = height;
        shapedPrimer.width = width;
        shapedPrimer.input = input;
        Object[] shape = RecipeParser.unparseShaped(shapedPrimer);
        return RecipeParser.convertShapedToRecipe(shape);
    }

    /**
     * Compile the aspects of a {@link Recipe}
     * @param aspects the {@link AspectList} to compile
     * @return a {@link HashMap} containing the aspects and amounts
     */
    private static HashMap<String, Integer> compileAspects(AspectList aspects) {
        HashMap<String, Integer> compiledAspects = new HashMap<>();
        if (aspects != null) {
            for (Aspect aspect : aspects.getAspects()) {
                compiledAspects.put(aspect.getName(), aspects.getAmount(aspect));
            }
        }
        return compiledAspects;
    }

}
