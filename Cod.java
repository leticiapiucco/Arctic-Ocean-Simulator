import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a cod.
 * Cods age, move, breed, and die.
 * 
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public class Cod extends Animal
{
    // Characteristics shared by all cods (class variables).

    // The age at which a cod can start to breed.
    private static final int BREEDING_AGE = 3;
    // The likelihood of a cod breeding.
    private static final double BREEDING_PROBABILITY = 0.087;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single cod. In effect, this is the
    // number of steps a cod can go before it has to eat again.
    private static final int ALGAE_FOOD_VALUE = 8;

    /**
     * Create a cod. A cod can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the cod will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Cod(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        MAX_AGE = 60; // The age to which a cod can live.
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
     * This is what the cod does most of the time - it looks for
     * algae. In the process, it might breed, die of hunger,
     * or die of old age.
     * 
     * @param newCods A list to return newly born cods.
     */
    public void dayAct(List<Animal> newCod)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newCod);            
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
     * This is what the cod does during the night - acts the same as during the day.
     * 
     * @param newCods A list to return newly born cods.
     */
    public void nightAct(List<Animal> newCod)
    {
        dayAct(newCod);
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
            Object animal = field.getObjectAt(where);
            if(animal instanceof Algae) {
                Algae algae = (Algae) animal;
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
     * Check whether or not this cod is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newCods A list to return newly born cods.
     */
    private void giveBirth(List<Animal> newCods)
    {
        // New cods are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(BREEDING_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Cod child = new Cod(false, field, loc);
            newCods.add(child);
        }
    }

}
