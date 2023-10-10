package nukeminecart.thaumicrecipe.recipes;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.internal.CommonInternals;

public class ChangeSmelting {
    public static void changeSmeltingBonus(Object in, ItemStack out, float chance) {
        if (in instanceof ItemStack || in instanceof String) {
            CommonInternals.smeltingBonus.add(new ThaumcraftApi.SmeltBonus(in, out, chance));
        }

    }
}
