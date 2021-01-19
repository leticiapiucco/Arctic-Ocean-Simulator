import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of plants.
 * 
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public abstract class Plant extends Life
{
    protected double AGE; // Plant's age
    protected double growRate; // The rate of plant's growth.
    protected int stepCount;    // Plant's age increase after every step
    
    /**
     * Create a new plant at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location)
    {
        super(field, location);
    }

    /**
     * Make this plant act.
     * 
     * @param newPlants A list to receive newly born plants.
     */
    abstract void act(List<Plant> newPlants);
    
    /**
     * Make this plant act when raining
     * 
     * @param newPlants A list to receive newly born plants.
     */
    abstract void rainAct(List<Plant> newPlants);
    
    /**
     * Make this plant act when snowing.
     */
    protected void snowAct()
    {
        incrementAge();
    }
    
    /**
     * Increase the age.
     * This could result in the plant's death.
     */
    protected void incrementAge() 
    {
        stepCount++;
        AGE = growRate * stepCount;
        if(AGE > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if the plant can breed.
     * 
     * @param breed_prob The plant's breeding probability.
     * @param max_litter Maximum number of births possible.
     * @return The number of births (may be zero).
     */
    protected int breed(double breed_prob, int max_litter)
    {
        int births = 0;
        if(rand.nextDouble() <= breed_prob) {
            births = rand.nextInt(max_litter) + 1;
        }
        return births;
    }
}
