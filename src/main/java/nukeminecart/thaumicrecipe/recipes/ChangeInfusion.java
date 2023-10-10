package nukeminecart.thaumicrecipe.recipes;

import net.minecraft.util.ResourceLocation;
import nukeminecart.thaumicrecipe.recipes.api.InfusionRecipeComplex;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IThaumcraftRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

public class ChangeInfusion {
    public static void changeInfusionRecipe(ResourceLocation location, int instability, Object input, AspectList aspects, Object[] recipe){
        InfusionRecipe infusionRecipe;
        IThaumcraftRecipe testRecipe = ThaumcraftApi.getCraftingRecipes().get(location);
        if(testRecipe instanceof InfusionRecipe) {
            infusionRecipe = (InfusionRecipe) testRecipe;
        }else{
            throw new IllegalArgumentException("Invalid Location");
        }
        ThaumcraftApi.addInfusionCraftingRecipe(location, new InfusionRecipe(
                infusionRecipe.getResearch(),
                infusionRecipe.getRecipeOutput(),
                instability,
                aspects,
                input,
                recipe));

    }
    public static void changeInfusionRecipe(ResourceLocation location, InfusionRecipe recipe){
        ThaumcraftApi.addInfusionCraftingRecipe(location, recipe);

    }
    public static void changeFakeInfusionRecipe(ResourceLocation location, InfusionRecipe recipe){
        ThaumcraftApi.addFakeCraftingRecipe(location, recipe);
    }
    public static void changeComplexInfusionRecipe(ResourceLocation location, int instability, Object input, AspectList aspects,Object[] recipe){
        InfusionRecipe infusionRecipe;
        IThaumcraftRecipe testRecipe = ThaumcraftApi.getCraftingRecipes().get(location);
        if(testRecipe instanceof InfusionRecipe) {
            infusionRecipe = (InfusionRecipe) testRecipe;
        }else{
            throw new IllegalArgumentException("Invalid Location");
        }
        ThaumcraftApi.addInfusionCraftingRecipe(location, new InfusionRecipeComplex(
                    infusionRecipe.getResearch(),
                    infusionRecipe.getRecipeOutput(),
                    instability,
                    aspects,
                    input,
                    recipe));

    }
}
