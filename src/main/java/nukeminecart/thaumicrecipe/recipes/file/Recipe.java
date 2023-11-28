package nukeminecart.thaumicrecipe.recipes.file;

import java.util.List;

/**
 * A type used in ThaumicRecipeTweakerGUI that contains information about a specific recipe
 */
public class Recipe {

    private String name, input, output, type, research, modid;
    private String[] ingredients, aspects, shape;
    private int vis;

    /**
     * Creates a new {@link Recipe} with the arguments specified
     *
     * @param name        the name
     * @param type        the type
     * @param research    the research required
     * @param input       the input item
     * @param ingredients a list of ingredients
     * @param output      the output item
     * @param vis         the vis cost
     * @param aspects     a list of aspects
     * @param shape       a {@link String} containing the shape
     */
    public Recipe(String name, String type, String research, String modid, String input, String[] ingredients, String output, int vis, String[] aspects, String... shape) {
        this.name = name;
        this.type = type;
        this.research = research;
        this.modid = modid;
        this.input = input;
        this.ingredients = ingredients;
        this.output = output;
        this.vis = vis;
        this.aspects = aspects;
        this.shape = shape;
    }

    /**
     * Copies the current {@link Recipe}
     *
     * @return the copied {@link Recipe}
     */
    public Recipe copy() {
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, shape);
    }

    /**
     * Gets the name of the recipe
     *
     * @return the name as a {@link String}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the recipe
     *
     * @param name the name as a {@link String}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the required research of the recipe
     *
     * @return the research as a {@link String}
     */
    public String getResearch() {
        return this.research;
    }

    /**
     * Sets the required research of the recipe
     *
     * @param research the research as a {@link String}
     */
    public void setResearch(String research) {
        this.research = research;
    }

    /**
     * Gets the type of recipe
     *
     * @return the type as a {@link String}
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type of recipe
     *
     * @param type the type as a {@link String}
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the input item of a recipe
     *
     * @return the input item as a {@link String}
     */
    public String getInput() {
        return this.input;
    }

    /**
     * Sets the input item of a recipe
     *
     * @param input the input item as a {@link String}
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Gets the ingredients of a recipe
     *
     * @return the ingredients as a {@link List<String>}
     */
    public String[] getIngredients() {
        return this.ingredients;
    }

    /**
     * Sets the ingredients of a recipe
     *
     * @param ingredients the ingredients as a varargs {@link String}
     */
    public void setIngredients(String... ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets the output item of a recipe
     *
     * @return the output item as a {@link String}
     */
    public String getOutput() {
        return this.output;
    }

    /**
     * Sets the output item of a recipe
     *
     * @param output the output item as a {@link String}
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * Gets the vis cost of a recipe
     *
     * @return the vis cost as a {@link Integer}
     */
    public int getVis() {
        return this.vis;
    }

    /**
     * Sets the vis cost of a recipe
     *
     * @param vis the vis cost as a {@link Integer}
     */
    public void setVis(int vis) {
        this.vis = vis;
    }

    /**
     * Gets the aspects of a recipe
     *
     * @return the aspects as a {@link List<String>}
     */
    public String[] getAspects() {
        return this.aspects;
    }

    /**
     * Sets the aspects of a recipe
     *
     * @param aspects the aspects as a varargs {@link String}
     */
    public void setAspects(String... aspects) {
        this.aspects = aspects;
    }

    /**
     * Gets the shape of a recipe
     *
     * @return the shape as a {@link String} array
     */
    public String[] getShape() {
        return this.shape;
    }

    /**
     * Sets the shape of a recipe
     *
     * @param shape the shape as a varargs {@link String}
     */
    public void setShape(String... shape) {
        this.shape = shape;
    }

    /**
     * Gets the modid of the recipe
     *
     * @return the modid as a {@link String}
     */
    public String getModid() {
        return this.modid;
    }

    /**
     * Sets the modid of the recipe
     *
     * @param modid the modid as a {@link String}
     */
    public void setModid(String modid) {
        this.modid = modid;
    }


}
