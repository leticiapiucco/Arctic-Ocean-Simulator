import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a plankton.
 * Planktons age, move, breed, and die.
 * 
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public class Plankton extends Animal
{
    // Characteristics shared by all planktons (class variables).

    // The age at which a plankton can start to breed.
    private static final int BREEDING_AGE = 3;
    // The likelihood of a plankton breeding.
    private static final double BREEDING_PROBABILITY = 0.081;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    
    // The food value of a single plankton. In effect, this is the
    // number of steps a plankton can go before it has to eat again.
    private static final int ALGAE_FOOD_VALUE = 8;

    /**
     * Create a plankton. A plankton can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the plankton will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plankton(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        MAX_AGE = 60; // The age to which a plankton can live.
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(ALGAE_FOOD_VALUE);
        }
        else{ 
            age = 0;
            foodLevel = ALGAE_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the plankton does during day - it looks for
     * plants. In the process, it might breed, die of hunger,
     * or die of old age
     * 
     * @param newplanktons A list to return newly born planktons.
     */
    public void dayAct(List<Animal> newPlankton)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newPlankton);            
            // Try to move into a free location.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * This is what the plankton does during the night - same as
     * during the day.
     * 
     * @param newplanktons A list to return newly born planktons.
     */
    public void nightAct(List<Animal> newPlankton)
    {
        dayAct(newPlankton);
    }
    
    /**
     * Look for algae adjacent to the current location.
     * Only the first found algae is eaten.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object plant = field.getObjectAt(where);
            if(plant instanceof Algae) {
                Algae algae = (Algae) plant;
                if(algae.isAlive()) { 
                    algae.setDead();
                    foodLevel = ALGAE_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this plankton is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newPlankton A list to return newly born plankton.
     */
    private void giveBirth(List<Animal> newPlanktons)
    {
        // New planktons are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(BREEDING_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Plankton child = new Plankton(false, field, loc);
            newPlanktons.add(child);
        }
    }

}