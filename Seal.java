import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a seal.
 * Seals age, move, eat cod, and die.
 * 
 * @author Leticia Piucco Marques and Ruobing Zao
 * @version 2020.02.23
 */
public class Seal extends Animal
{
    // Characteristics shared by all seals (class variables).

    // The age at which a seal can start to breed.
    private static final int BREEDING_AGE = 6;      
    // The likelihood of a seal breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single cod. In effect, this is the
    // number of steps a seal can go before it has to eat again.
    private static final int COD_FOOD_VALUE = 17;

    /**
     * Create a seal. A seal can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the seal will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Seal(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        cureProb = 0.08; // Probability of cure an infected seal.
        MAX_AGE = 70;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(COD_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = COD_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the seal does during the night: it hunts for
     * cods. In the process, it might breed, die of hunger,
     * or die of old age.
     * 
     * @param newSeals A list to return newly born seals.
     */
    public void nightAct(List<Animal> newSeals)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newSeals);
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
     * Look for cods adjacent to the current location.
     * Only the first live cod is eaten.
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
            if(animal instanceof Cod) {
                Cod cod = (Cod) animal;
                if(cod.isAlive()) { 
                    cod.setDead();
                    foodLevel = COD_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this seal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newSeals A list to return newly born seals.
     */
    private void giveBirth(List<Animal> newSeals)
    {
        // New seals are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(BREEDING_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
        if(breedWithNeighbour(this)){
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Seal child = new Seal(false, field, loc);
                newSeals.add(child);
            }
        }
    }

}
