package nukeminecart.thaumicrecipe.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.GameData;

public class ChangeNormal {

    public static void changeShapedNormalRecipe(ResourceLocation location, ResourceLocation group, ItemStack output,Object... recipe){
        GameRegistry.addShapedRecipe(location, group, output, recipe);
    }
    public static void changeShapelessNormalRecipe(ResourceLocation location, ResourceLocation group, ItemStack output, Ingredient... recipe){
        GameRegistry.addShapelessRecipe(location, group, output, recipe);
    }
    public static void changeShapedOreNormalRecipe(ResourceLocation location, ResourceLocation group, ItemStack output, Object... recipe){
        IRecipe rec = new ShapedOreRecipe(group, output, recipe);
        rec.setRegistryName(location);
        GameData.register_impl(rec);
    }
    public static void changeShapelessOreNormalRecipe(ResourceLocation location, ResourceLocation group, ItemStack output, Ingredient... recipe){
        IRecipe rec = new ShapelessOreRecipe(group, output, (Object[]) recipe);
        rec.setRegistryName(location);
        GameData.register_impl(rec);
    }

}
