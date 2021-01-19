import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public abstract class Animal extends Life
{
    // Whether the animal is female or not.
    private boolean female;
    // Whether the animal is infected by disease or not.
    private boolean infected = false;
    // Whether the ill animal is cured or not.
    private boolean cured = false;
    // The likelihood of ill animal being cured.
    protected double cureProb;
    // Maximum number of animals can be infected by one ill animal.
    private int maxInfection;
    // Number of days the animal has been infected.    
    private int infectedDay;   
    // The animal's food level, which is increased by eating food.
    protected int foodLevel;

    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        super(field, location);
        female = rand.nextBoolean();
        maxInfection = rand.nextInt(5); 
    }

    /**
     * Make this animal act during the day.
     * 
     * @param newAnimals A list to receive newly born animals.
     */
    protected void dayAct(List<Animal> newAnimals)
    {
        incrementAge();
        incrementHunger();
    }

    /**
     * Make this animal act during the night.
     * 
     * @param newAnimals A list to receive newly born animals.
     */
    protected void nightAct(List<Animal> newAnimals)
    {
        incrementAge();
        incrementHunger();
    }

    /**
     * Make this animal act when snowing.
     */
    protected void snowAct()
    {
        incrementAge();
        incrementHunger();
    }

    /**
     * Check whether the animal is a female.
     * 
     * @return variable female.
     */
    private boolean isFemale()
    {
        return female;
    }

    /**
     * Check whether two animals are of different genders.
     * 
     * @param animal1 One of the animals that is being compared.
     * @param animal2 Another animal that is being compared.
     */
    private boolean isDifferentGender(Animal animal1, Animal animal2)
    {
        if(animal1.isFemale() != animal2.isFemale()) {
            return true;
        }
        return false;
    }

    /**
     * Increase the age.
     * This could result in the animal's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    } 

    /**
     * Make this Animal more hungry. This could result in the Animal's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Check whether the animal can breed or not.
     * 
     * @return true if its current age is equal or greater than the breeding age.
     */
    private boolean canBreed(int breed_age)
    {
        return age >= breed_age;
    }

    /**
     * Check whether the animal can breed with its neighbour, i.o if the neighbour is
     * of the same species and different gender.
     * 
     * @param oneAnimal The animal being checked.
     */
    protected boolean breedWithNeighbour(Animal oneAnimal)
    {
        List<Location> neighbour = getField().adjacentLocations(getLocation());
        for(int i = 0; i < neighbour.size(); i++) {
            Object newObject = getField().getObjectAt(neighbour.get(i));
            if((newObject != null) && isSameSpecies(oneAnimal, newObject)){
                Animal newAnimal = (Animal) newObject;
                if(isDifferentGender(oneAnimal, newAnimal)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Generate a number representing the number of births,
     * if the animal can breed.
     * 
     * @param breed_age Breeding age of the animal.
     * @param breed_prob Breeding probabilty of the animal.
     * @param max_litter Maximum number of births.
     * @return The number of births (may be zero).
     */
    protected int breed(int breed_age,double breed_prob, int max_litter)
    {
        int births = 0;
        if(canBreed(breed_age) && rand.nextDouble() <= breed_prob) {
            births = rand.nextInt(max_litter) + 1;
        }
        return births;
    }

    /**
     * Check whether the animal is infectable by disease or not.
     * 
     * @return true if it is infecable.
     */
    public boolean canBeInfected()
    {
        if(!infected && !cured) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Change the infection state of an animal.
     */
    public void beingInfected()
    {
        infected = true;
    }

    /**
     * Make this animal act when it is infected by disease and
     * change the color of cured animal into its original color.
     * 
     * @param view Object of SimulatorView.
     */
    public void infectAndCure(SimulatorView view)
    {
        if(infected) {
            infectedDay++;
            if(infectedDay >= 5) {
                setDead();
            }
            
            // Try to cure the infected animal.
            else if(rand.nextDouble() < cureProb) {
                infected = false;
                cured = true;
                // Set the color of cured animal back to its origional color.
                view.setIndividualColor(this, view.getClassColor(this.getClass()));
            }
            
            else {
                infectNeighbour(view);
            }
        }
    }

    /**
     * Infect neibour animal of same species when an animal is ill.
     * 
     * @param view Object of SimulatorView.
     */
    private void infectNeighbour(SimulatorView view)
    {
        List<Location> neighbour = getField().adjacentLocations(getLocation());

        for(int j = 0; (j < neighbour.size()) && (j < maxInfection); j++) {
            Object neighbourObj = getField().getObjectAt(neighbour.get(j));
            if(neighbourObj instanceof Animal) {
                Animal neighbourAnimal = (Animal)neighbourObj; 
                if(isSameSpecies(this, neighbourAnimal) && neighbourAnimal.canBeInfected()) {
                    neighbourAnimal.infectAndCure(view);
                } 
            }
        }
    }

}
