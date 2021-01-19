import java.util.*;
import java.awt.Color;
import java.util.Random;

/**
 * A predator-prey simulator of the Arctic Ocean,
 * based on a rectangular field containing animals and plants.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 240;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 160;
    // The probability that the animals  will be created in any given grid position.
    private static final double SEAL_CREATION_PROBABILITY = 0.05;
    private static final double COD_CREATION_PROBABILITY = 0.06;
    private static final double POLARBEAR_CREATION_PROBABILITY = 0.05;
    private static final double WHALE_CREATION_PROBABILITY = 0.05;
    private static final double PLANKTON_CREATION_PROBABILITY = 0.06;
    private static final double ALGAE_CREATION_PROBABILITY = 0.09;

    // Lists of animals and plants in the field.
    private List<Animal> animals;
    private List<Plant> plants;

    private Field field;        // The current state of the field.
    private int step;           // The current step of the simulation.
    private SimulatorView view; // A graphical view of the simulation.

    private Weather weather;    // The weather conditions
    private Disease disease;    // The diseases that can infect animals
    private int counter;        // A counter for updating the daytime and weather

    // Current state of the day.
    private boolean day = true;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        plants = new ArrayList<>();
        field = new Field(depth, width);
        weather = new Weather();
        disease = new Disease();

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Cod.class, Color.ORANGE);
        view.setColor(Seal.class, Color.LIGHT_GRAY);
        view.setColor(PolarBear.class, Color.RED);
        view.setColor(Whale.class, Color.BLUE);
        view.setColor(Plankton.class, Color.pink);
        view.setColor(Algae.class, Color.GREEN);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period.
     */
    public void runLongSimulation()
    {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * 
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(50);
        }
    }    

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of the day, animals and plants.
     */
    public void simulateOneStep()
    {
        //Update daytime and weather
        conditions();
        
        step++;
        
        // Provide space for newborn animals and plants.
        List<Animal> newAnimals = new ArrayList<>(); 
        List<Plant> newPlants = new ArrayList<>();
        
        // Let all animal act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();

            //Might result in the animal being infected
            if(animal.canBeInfected()) {
                disease.infectAnimal(animal,view);
            }
            
            // Update condition of infected animals
            animal.infectAndCure(view);
            
            //Act during the day
            if(day){
                //Act when according to the current weather
                if(weather.isSunny() || weather.isRaining()){
                    animal.dayAct(newAnimals);
                }
                else if(weather.isSnowing()){
                    animal.snowAct();
                }
            }
            //Act during the night
            else{
                animal.nightAct(newAnimals);
            }  
            
            if(! animal.isAlive()) {
                it.remove();
            }
        }
 
        // Let all plant act.
        for(Iterator<Plant> it = plants.iterator(); it.hasNext(); ) {
            Plant plant = it.next();
            
            //Act according to current weather
            if(weather.isRaining()){
                plant.rainAct(newPlants);
            }
            else if(weather.isSnowing()){
                plant.snowAct();
            }
            else{
                plant.act(newPlants);
            }
            if(! plant.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);
        plants.addAll(newPlants);

        view.showStatus(step, field, weather, day);
    } 

    /**
     * Change the daytime and weather every 7 steps.
     */
    private void conditions(){
        counter++;
        if(counter == 7){
            day = !day;
            weather.update();
            counter = 0;
        }
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field, weather, day);
    }

    /**
     * Randomly populate the field with animals and plants.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                // Populate animals.
                if(rand.nextDouble() <= POLARBEAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    PolarBear bear = new PolarBear(true, field, location);
                    animals.add(bear);
                }

                else if(rand.nextDouble() <= SEAL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Seal seal = new Seal(true, field, location);
                    animals.add(seal);
                }

                else if(rand.nextDouble() <= WHALE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Whale whale = new Whale(true, field, location);
                    animals.add(whale);
                }

                else if(rand.nextDouble() <= COD_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Cod cod = new Cod(true, field, location);
                    animals.add(cod);
                }

                else if(rand.nextDouble() <= PLANKTON_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plankton plankton = new Plankton(true, field, location);
                    animals.add(plankton);
                }

                //Populate plants.
                else if(rand.nextDouble() <= ALGAE_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Algae algae = new Algae(true, field, location);
                    plants.add(algae);
                }
            }
        }

    }

    /**
     * Pause for a given time.
     * 
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
