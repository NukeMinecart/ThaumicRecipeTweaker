package nukeminecart.thaumicrecipe.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

public class ChangeArcane {
    public static void changeShapedArcaneRecipe(ResourceLocation location, ResourceLocation group, int vis, AspectList crystals, Object... recipe){
        ShapedArcaneRecipe arcaneRecipe;
        IRecipe testRecipe = GameRegistry.findRegistry(IRecipe.class).getValue(location);
        if(testRecipe instanceof IArcaneRecipe) {
            arcaneRecipe = (ShapedArcaneRecipe) testRecipe;
        }else{
            throw new IllegalArgumentException("Invalid Location");
        }
        ThaumcraftApi.addArcaneCraftingRecipe(location,new ShapedArcaneRecipe(group,
                arcaneRecipe.getResearch(),
                vis,
                crystals,
                arcaneRecipe.delegate.get().getRecipeOutput(), recipe));
    }
    public static void changeShapelessArcaneRecipe(ResourceLocation location, ResourceLocation group, int vis,AspectList aspects, Object... recipe){
        ShapelessArcaneRecipe arcaneRecipe;
        IRecipe testRecipe = GameRegistry.findRegistry(IRecipe.class).getValue(location);
        if(testRecipe instanceof IArcaneRecipe) {
            arcaneRecipe = (ShapelessArcaneRecipe) testRecipe;
        }else{
            throw new IllegalArgumentException("Invalid Location");
        }
        ThaumcraftApi.addArcaneCraftingRecipe(location,new ShapelessArcaneRecipe(group,
                arcaneRecipe.getResearch(),
                vis,
                aspects,
                arcaneRecipe.delegate.get().getRecipeOutput(), recipe));
    }
}
