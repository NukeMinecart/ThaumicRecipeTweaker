package nukeminecart.thaumicrecipe.recipes;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IThaumcraftRecipe;

public class ChangeAlchemy {
    public static void changeAlchemyRecipe(ResourceLocation location, Object catalyst, AspectList aspects){
        CrucibleRecipe alchemyRecipe;
        IThaumcraftRecipe testRecipe = ThaumcraftApi.getCraftingRecipes().get(location);
        if(testRecipe instanceof CrucibleRecipe) {
            alchemyRecipe = (CrucibleRecipe) testRecipe;
        }else{
            throw new IllegalArgumentException("Invalid Location");
        }
        ThaumcraftApi.addCrucibleRecipe(location, new CrucibleRecipe(
                alchemyRecipe.getResearch(),
                alchemyRecipe.getRecipeOutput(),
                catalyst,
                aspects));
    }

}
