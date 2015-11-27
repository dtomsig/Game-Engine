public abstract class Unit 
{
    private int health;
    
    public void reduceHealth(int incomingHealth)
    {
        this.health -= incomingHealth;
    }
}
