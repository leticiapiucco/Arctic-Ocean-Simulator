import java.util.Random;
import java.util.List;

/**
 * A class representing shared characteristics of forms of life.
 *
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public abstract class Life
{
    // Whether the creature is alive or not.
    private boolean alive;
    private Field field;
    private Location location; 
    protected int age;
    // Maximum age the creature can live up to before dying.
    protected int MAX_AGE;
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Create a new creature at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Life(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }

    /**
     * Increase the creature's age.
     */
    abstract void incrementAge();
    
    /**
     * Place the creature at the new location in the given field.
     * 
     * @param newLocation The creature's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /** 
     * @return The creature's location.
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * @return The creature's field.
     */
    protected Field getField()
    {
        return field;
    } 

    /**
     * Check whether two creatures are of the same species.
     * @param Creatures that are being compared.
     */
    protected boolean isSameSpecies(Object obj1, Object obj2)
    {
        if(obj1.getClass() == obj2.getClass()){
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Check whether the creature is alive or not.
     * 
     * @return true if it is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the creature is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
}
