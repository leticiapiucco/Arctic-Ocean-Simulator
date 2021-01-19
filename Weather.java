import java.util.Random;

/**
 * A class representing the weather.
 *
 * @author Leticia Piucco Marques and Ruobing Zhao
 * @version 2020.02.23
 */
public class Weather
{
    private int temperature; 
    
    // The possible weather states of the simulation
    private boolean sunny;
    private boolean raining = false;
    private boolean snowing = false;
    
    private Random random = Randomizer.getRandom();

    /**
     * Creates a weather with a random temperature and
     * sets truth-value to sunny. If not sunny, the temperature
     * determines if it is raining or snowing.
     */
    public Weather()
    {
       temperature = random.nextInt(40);
       
       sunny = random.nextBoolean();
       if(!sunny){
           if(temperature > 20){
               raining = true;
           }
           else{
               snowing = true;  
           }
       }
    }
    
    /**
     * Changes the values of the temperature and weather conditions.
     */
    public void update(){
       temperature = random.nextInt(40);
       sunny = random.nextBoolean();
       if(!sunny){
           if(temperature > 20){
               raining = true;
           }
           else{
               snowing = true;  
           }
       }
    }
    
    /**
     * @return The current temperature.
     */
    public int getTemperature()
    {
        return temperature;
    }
    
    /**
     * Check if it is sunny or not.
     * 
     * @return sunny's value.
     */
    public boolean isSunny(){
        return sunny;
    }
    
    /**
     * Check if it is raining or not.
     * 
     * @return snowing's value.
     */
    public boolean isSnowing(){
        return snowing;
    }
    
    /**
     * Check if it is raining or not.
     * 
     * @return raining's value.
     */
    public boolean isRaining(){
        return raining;
    }
    
    /**
     * Current weather is transformed in its current state.
     * Method used in SimulatorView
     * 
     * @return string of the current weather's state.
     */
    public String toString(){
        if(sunny){
            return "Sunny";
        }
        else if(raining){
            return "Raining";
        }
        else if(snowing){
            return "Snowing";
        }
        return "";
    }
}
