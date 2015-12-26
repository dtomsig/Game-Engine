public abstract class Unit 
{
    private int health;
    
    public void reduceHealth(int incomingDamage)
    {
        this.health -= incomingDamage;
    }
}
