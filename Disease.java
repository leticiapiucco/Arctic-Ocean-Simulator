import java.util.Random;
import java.util.ArrayList;
import java.awt.Color; 

/**
 * A collection of diseases that can spread amoung animals.
 * Certain disease can infect certain animals and change their color presented on screen.
 *
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public class Disease
{
    // Infection probability of disease PDH.
    private static final double PDHInfectProb = 0.03;
    
    // Collection of animals that can be infected by PDH.
    private ArrayList<Class> PDHList = new ArrayList<>();
    
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create the diseases. A disease can infect certain classes of animal.
     */
    public Disease()
    {
        PDHList.add(Seal.class);
    }
    
    /**
     * One kind of disease can infect certain animal and change its color.
     * 
     * @param animal Animal being infected.
     * @param view Object of SimuloarView.
     */
    public void infectAnimal(Animal animal, SimulatorView view)
    {
        Class animalClass = animal.getClass();
        if(canInfect(animalClass, PDHList, PDHInfectProb)) {
            animal.beingInfected(); // Change the boolean "infected" in animal to True
            view.setIndividualColor(animal, Color.BLACK); // Change infected animal's color
        }
    }

    /**
     * Check whether the disease can infect an animal species or not.
     * 
     * @param animalClass Species of the animal being checked.
     * @param diseaseList Collection of infectable animal species.
     * @param infectProb Probability of infection.
     * @return true if disease can infect the animal species.
     */
    private boolean canInfect(Class animalClass, ArrayList<Class> diseaseList, double infectProb)
    {
        if(diseaseList.contains(animalClass) && (rand.nextDouble() < infectProb))  {
            return true;
        }
        return false;
    }
}
