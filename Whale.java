import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a whale.
 * Whales age, move, eat plankton, and die.
 * 
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public class Whale extends Animal
{
    // Characteristics shared by all whales (class variables).
    
    // The age at which a whale can start to breed.
    private static final int BREEDING_AGE = 8;
    // The likelihood of a whale breeding.
    private static final double BREEDING_PROBABILITY = 0.21;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single whale. In effect, this is the
    // number of steps a whale can go before it has to eat again.
    private static final int PLANKTON_FOOD_VALUE = 16;

    /**
     * Create a whale. A whale can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the whale will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Whale(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        MAX_AGE = 120; // The age to which a Whale can live.
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANKTON_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PLANKTON_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the whale does during the day: it hunts for
     * planktons. In the process, it might breed, die of hunger,
     * or die of old age.
     * 
     * @param newWhales A list to return newly born whales.
     */
    public void dayAct(List<Animal> newWhales)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newWhales);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Look for planktons adjacent to the current location.
     * Only the first live plankton is eaten.
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
            if(animal instanceof Plankton) {
                Plankton plankton = (Plankton) animal;
                if(plankton.isAlive()) { 
                    plankton.setDead();
                    foodLevel = PLANKTON_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this whale is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newWhales A list to return newly born whales.
     */
    private void giveBirth(List<Animal> newWhales)
    {
        // New whales are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(BREEDING_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
        if(breedWithNeighbour(this)){
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Whale child = new Whale(false, field, loc);
                newWhales.add(child);
            }
        }
    }

}
