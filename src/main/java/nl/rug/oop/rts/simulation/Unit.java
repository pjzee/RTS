package nl.rug.oop.rts.simulation;

/**
 * Unit class. Has a name, the damage it deals and its health. Belongs to an army.
 */
public class Unit {
    private String name;
    private int damage;
    private int health;
    private Army army;

    /**
     * Constructor for unit.
     * @param name The name of this unit.
     * @param damage The damage this unit deals.
     * @param health The health this unit starts with.
     * @param army The army this unit belongs to.
     */
    public Unit(String name, int damage, int health, Army army) {
        this.name = name;
        this.damage = damage;
        this.health = health;
        this.army = army;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    /**
     * Take damage: subtract damage from health.
     * It health smaller equal zero, remove this unit from the army as it has 'died'.
     * @param damage The amount of damage the unit takes.
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            army.removeUnit(this);
        }
    }

    public int getHealth() {
        return health;
    }
}
