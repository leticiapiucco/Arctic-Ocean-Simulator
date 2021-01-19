import java.util.List;

/**
 * A simple model of a Algae.
 * Algaes age, move, grow, and die.
 *
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public class Algae extends Plant
{
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The likelihood of a algea breed.
    private static double BREEDING_PROBABILITY = 0.119;
    
    /**
     * Create an algae. An algae can be created as a new born (age zero) 
     * or with a random age.
     * 
     * @param randomAge If true, the Whale will have random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Algae(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        growRate = 0.45;
        MAX_AGE = 30;
        if(randomAge) {
            AGE = rand.nextInt(MAX_AGE);
        }
        else {
            AGE = 0;
        }
    }
    
    /**
     * This is what the cod does most of the time - it grows.
     * In the process, it might breed or die of old age.
     * 
     * @param newAlgae A list to return newly born algae.
     */
    public void act(List<Plant> newAlgae)
    {
        growRate = 0.45;
        incrementAge();
        if(isAlive()) {
            sprout(newAlgae);
        }
    }
    
    /**
     * The algae grows faster when it rains.
     * In the process, it might breed or die of old age.
     * 
     * @param newAlgae A list to return newly born algae.
     */
    public void rainAct(List<Plant> newAlgae)
    {        
        growRate += 0.2;
        incrementAge();
        if(isAlive()) {
            sprout(newAlgae);
        }
    }
    
    /**
     * Check whether or not this algae is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newAlgae A list to return newly born algae.
     */
    private void sprout(List<Plant> newAlgae)
    {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(BREEDING_PROBABILITY, MAX_LITTER_SIZE);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Algae child = new Algae(false, field, loc);
            newAlgae.add(child);
        }
    }
}
