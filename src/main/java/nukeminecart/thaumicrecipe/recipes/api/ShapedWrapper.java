package nukeminecart.thaumicrecipe.recipes.api;

import net.minecraft.item.crafting.IRecipe;

public class ShapedWrapper {
    private final IRecipe recipe;
    private final Object[] shape;
    public ShapedWrapper(IRecipe recipe, Object[] shape){
        this.recipe = recipe;
        this.shape = shape;
    }

    public IRecipe getRecipe(){
        return recipe;
    }
    public Object[] getShape(){
        return shape;
    }
}
