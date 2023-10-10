package nukeminecart.thaumicrecipe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nukeminecart.thaumicrecipe.ui.HomeUI;

import javax.swing.*;

@Mod(modid = ThaumicRecipeTweaker.MODID, useMetadata = true)
public class ThaumicRecipeTweaker
{
    public static final String MODID = "thaumicrecipe";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        new HomeUI();
    }
}
