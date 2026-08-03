// Aiden Tsang
// CS213 Lab 6

import java.util.Scanner;
import java.util.Random;

final class Constants {
    private Constants() {
    }

    public static final int MIN_STAT = 50;
    public static final int MAX_STAT = 197;
    public static final int STAT_RANGE = MAX_STAT - MIN_STAT + 1;
    public static final int MAX_ARMY_SIZE = 12;   // 24 names split evenly between 2 armies; enlarge DEFAULT_NAMES to raise this
    public static final int MIN_ARMY_SIZE = 1;
    public static final int MIN_VALID_SIZE = 0;
    public static final int MIN_VALID_HEALTH = 0;
    public static final int DEFAULT_ARMY_SIZE = 0;
    public static final int DUMMY_VALUE = -1;
    public static final String DEFAULT_NAME = "n/a";
    public static final String DEFAULT_ARMY_NAME = "Unnamed";
    public static final int BAHAMUT_BONUS_DAMAGE = 25;
    public static final int BAHAMUT_BONUS_CHANCE = 10;   // percent out of PERCENT_ROLL
    public static final int MACARA_BONUS_CHANCE = 20;   // percent out of PERCENT_ROLL; lab text said 20%, its sample code computed 5%
    public static final int PERCENT_ROLL = 100;
    public static final int MACARA_MULTIPLIER = 2;
    public static final String ARMY1_NAME = "Army 1";
    public static final String ARMY2_NAME = "Army 2";

    public static final String[] DEFAULT_NAMES = {
            "Theron", "Thorfin", "Petra", "Karan", "Seren", "Lunara",
            "Lagnar", "Orrin", "Quillon", "Morwen", "Nyx", "Chester",
            "Ragnar", "Kaelith", "Cookie", "Aldric", "Grisha", "Isolde",
            "Scrandal", "Dorian", "Hollis", "Faelan", "Clamar", "Cassia"
    };
}

enum CreatureType {
    BAHAMUT("bahamut"),
    MACARA("macara"),
    CYBERBAHAMUT("cyberbahamut"),
    SUPERBAHAMUT("superbahamut");

    private final String description;

    CreatureType(String newDescription) {
        description = newDescription;
    }

    public String getDescription() {
        return description;
    }
}

abstract class Creature {
    protected String name = Constants.DEFAULT_NAME;
    protected int health = Constants.MIN_STAT;
    protected int strength = Constants.MIN_STAT;
    protected static final Random rand = new Random();

    public Creature() {
        setCreature(Constants.DEFAULT_NAME, Constants.MIN_STAT, Constants.MIN_STAT);
    }

    public Creature(String newName, int newHealth, int newStrength) {
        setCreature(newName, newHealth, newStrength);
    }

    public void setCreature(String newName, int newHealth, int newStrength) {
        if (newName == null || newHealth < Constants.MIN_VALID_HEALTH || newHealth > Constants.MAX_STAT || newStrength < Constants.MIN_STAT || newStrength > Constants.MAX_STAT) {
            System.out.println("\nInvalid creature data; keeping current values");
        }
        else {
            name = newName;
            health = newHealth;
            strength = newStrength;
        }
    }

    public void setName(String newName) {
        setCreature(newName, health, strength);
    }

    public void setHealth(int newHealth) {
        setCreature(name, newHealth, strength);
    }

    public void setStrength(int newStrength) {
        setCreature(name, health, newStrength);
    }

    public String getName() {
        return name + " the " + getCreatureType();
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public abstract String getCreatureType();

    public int getDamage() {
        int damage = 0;
        if (strength > 0) {
            damage = rand.nextInt(strength) + 1;
        }
        return damage;
    }

    public String toString() {
        return String.format("%-15s %-20s %10d %10d", name, getCreatureType(), strength, health);
    }
}

class Bahamut extends Creature {

    public Bahamut() {
        super();
    }

    public Bahamut(String newName, int newHealth, int newStrength) {
        super(newName, newHealth, newStrength);
    }

    public String getCreatureType() {
        return CreatureType.BAHAMUT.getDescription();
    }

    public int getDamage() {
        int damage = super.getDamage();
        if (rand.nextInt(Constants.PERCENT_ROLL) < Constants.BAHAMUT_BONUS_CHANCE) {
            damage = damage + Constants.BAHAMUT_BONUS_DAMAGE;
        }
        return damage;
    }
}

class Cyberbahamut extends Bahamut {

    public Cyberbahamut() {
        super();
    }

    public Cyberbahamut(String newName, int newHealth, int newStrength) {
        super(newName, newHealth, newStrength);
    }

    public String getCreatureType() {
        return CreatureType.CYBERBAHAMUT.getDescription();
    }
}

class Superbahamut extends Bahamut {

    public Superbahamut() {
        super();
    }

    public Superbahamut(String newName, int newHealth, int newStrength) {
        super(newName, newHealth, newStrength);
    }

    public String getCreatureType() {
        return CreatureType.SUPERBAHAMUT.getDescription();
    }

    public int getDamage() {
        return super.getDamage() + super.getDamage();
    }
}

class Macara extends Creature {

    public Macara() {
        super();
    }

    public Macara(String newName, int newHealth, int newStrength) {
        super(newName, newHealth, newStrength);
    }

    public String getCreatureType() {
        return CreatureType.MACARA.getDescription();
    }

    public int getDamage() {
        int damage = super.getDamage();
        if (rand.nextInt(Constants.PERCENT_ROLL) < Constants.MACARA_BONUS_CHANCE) {
            damage = damage * Constants.MACARA_MULTIPLIER;
        }
        return damage;
    }
}

class Army {
    private String armyName = Constants.DEFAULT_ARMY_NAME;
    private int size = Constants.DEFAULT_ARMY_SIZE;
    private Creature[] creatures = new Creature[Constants.MAX_ARMY_SIZE];
    private boolean[] usedNames = new boolean[0];
    private String[] creatureNames = new String[0];
    private static final Random rand = new Random();

    public Army() {
        setArmy(Constants.DEFAULT_ARMY_NAME, Constants.DEFAULT_ARMY_SIZE, Constants.DEFAULT_NAMES, new boolean[Constants.DEFAULT_NAMES.length]);
    }

    public Army(String newArmyName) {
        setArmy(newArmyName, Constants.DEFAULT_ARMY_SIZE, Constants.DEFAULT_NAMES, new boolean[Constants.DEFAULT_NAMES.length]);
    }

    private void setArmy(String newArmyName, int newSize, String[] namesArray, boolean[] sharedUsedNames) {
        boolean namesMatch = namesArray != null && sharedUsedNames != null && namesArray.length == sharedUsedNames.length;
        boolean isValid = newArmyName != null && newSize >= Constants.MIN_VALID_SIZE && newSize <= Constants.MAX_ARMY_SIZE && namesMatch;

        if (!isValid) {
            System.out.println("Invalid army data; keeping current values");
        }
        else {
            armyName = newArmyName;
            size = newSize;
            creatureNames = namesArray;
            usedNames = sharedUsedNames;
            for (int i = 0; i < Constants.MAX_ARMY_SIZE; i++) {
                if (i < size) {
                    creatures[i] = createRandomCreature();
                }
                else {
                    creatures[i] = new Macara();
                }
            }
        }
    }

    public void createArmy(String newArmyName, int newSize, String[] namesArray, boolean[] sharedUsedNames) {
        setArmy(newArmyName, newSize, namesArray, sharedUsedNames);
    }

    private Creature createRandomCreature() {
        Creature result = null;
        CreatureType[] types = CreatureType.values();
        CreatureType randomType = types[rand.nextInt(types.length)];

        String uniqueName = getUniqueName();
        int health = Constants.MIN_STAT + rand.nextInt(Constants.STAT_RANGE);
        int strength = Constants.MIN_STAT + rand.nextInt(Constants.STAT_RANGE);

        switch (randomType) {
            case BAHAMUT:
                result = new Bahamut(uniqueName, health, strength);
                break;
            case CYBERBAHAMUT:
                result = new Cyberbahamut(uniqueName, health, strength);
                break;
            case SUPERBAHAMUT:
                result = new Superbahamut(uniqueName, health, strength);
                break;
            case MACARA:
                result = new Macara(uniqueName, health, strength);
                break;
        }

        return result;
    }

    private boolean areAllNamesUsed() {
        boolean allUsed = true;
        for (int i = 0; i < usedNames.length && allUsed; i++) {
            if (!usedNames[i]) {
                allUsed = false;
            }
        }
        return allUsed;
    }

    private String getUniqueName() {
        String resultName;

        if (creatureNames.length == 0 || areAllNamesUsed()) {
            resultName = "Warrior_" + (rand.nextInt(1000) + 1);
        }
        else {
            int startPos = rand.nextInt(creatureNames.length);
            int index = startPos;
            while (usedNames[index]) {
                index = (index + 1) % creatureNames.length;
            }
            usedNames[index] = true;
            resultName = creatureNames[index];
        }

        return resultName;
    }

    public Creature getCreature(int index) {
        return creatures[index];
    }

    public int getSize() {
        return size;
    }

    public String getArmyName() {
        return armyName;
    }

    public int getTotalHealth() {
        int total = 0;
        for (int i = 0; i < size; i++) {
            total = total + creatures[i].getHealth();
        }
        return total;
    }

    public void reset() {
        setArmy(Constants.DEFAULT_ARMY_NAME, Constants.DEFAULT_ARMY_SIZE, Constants.DEFAULT_NAMES, new boolean[Constants.DEFAULT_NAMES.length]);
    }

    public void printStats(String label) {
        String header = "\n" + armyName + " Stats " + label + "\n" + String.format("%-15s %-20s %10s %10s", "Creature", "Type", "Strength", "Health");
        System.out.println(header);

        for (int i = 0; i < size; i++) {
            System.out.println(creatures[i].toString());
        }

        System.out.println("Total health of " + armyName + ": " + getTotalHealth());
    }
}

class Game {
    private Army army1 = new Army(Constants.ARMY1_NAME);
    private Army army2 = new Army(Constants.ARMY2_NAME);
    private Random rand = new Random();

    public void play(Scanner input) {
        int armySize = askArmySize(input);
        boolean[] sharedUsedNames = new boolean[Constants.DEFAULT_NAMES.length];

        army1.createArmy(Constants.ARMY1_NAME, armySize, Constants.DEFAULT_NAMES, sharedUsedNames);
        army2.createArmy(Constants.ARMY2_NAME, armySize, Constants.DEFAULT_NAMES, sharedUsedNames);

        System.out.println("\n########## NEW BATTLE ##########");
        army1.printStats("before the Battle");
        army2.printStats("before the Battle");

        System.out.println("\n" + String.format("%-25s %8s  %-8s %-25s %18s  %-8s", "Attacker", "Damage", "Army", "Defender", "Defender's Health", "Army"));

        int pairIndex = 0;
        while (pairIndex < army1.getSize() && pairIndex < army2.getSize()) {
            runDuel(army1.getCreature(pairIndex), army2.getCreature(pairIndex), pairIndex + 1);
            pairIndex++;
        }

        army1.printStats("after the Battle");
        army2.printStats("after the Battle");
        announceWinner();

        army1.reset();
        army2.reset();
    }

    private int askArmySize(Scanner input) {
        int size = Constants.DUMMY_VALUE;
        while (size < Constants.MIN_ARMY_SIZE || size > Constants.MAX_ARMY_SIZE) {
            System.out.print("\nHow many creatures per army (" + Constants.MIN_ARMY_SIZE + "-" + Constants.MAX_ARMY_SIZE + ")? ");
            if (input.hasNextInt()) {
                size = input.nextInt();
            }
            else {
                input.nextLine();
                size = Constants.DUMMY_VALUE;
            }
            if (size < Constants.MIN_ARMY_SIZE || size > Constants.MAX_ARMY_SIZE) {
                System.out.println("Invalid input. Please enter a number between " + Constants.MIN_ARMY_SIZE + " and " + Constants.MAX_ARMY_SIZE + ".");
            }
        }
        input.nextLine();
        return size;
    }

    private void runDuel(Creature creature1, Creature creature2, int duelNumber) {
        Creature attacker = creature1;
        Creature defender = creature2;
        String attackerArmy = Constants.ARMY1_NAME;
        String defenderArmy = Constants.ARMY2_NAME;

        if (rand.nextInt(2) == 1) {
            attacker = creature2;
            defender = creature1;
            attackerArmy = Constants.ARMY2_NAME;
            defenderArmy = Constants.ARMY1_NAME;
        }

        System.out.println("\n--- Duel #" + duelNumber + ": " + creature1.getName() + " (" + Constants.ARMY1_NAME + ") vs " + creature2.getName() + " (" + Constants.ARMY2_NAME + ") ---\n" + attacker.getName() + " attacks first!");

        while (creature1.getHealth() > 0 && creature2.getHealth() > 0) {
            performStrike(attacker, attackerArmy, defender, defenderArmy);

            Creature tempCreature = attacker;
            attacker = defender;
            defender = tempCreature;
            String tempArmy = attackerArmy;
            attackerArmy = defenderArmy;
            defenderArmy = tempArmy;
        }

        boolean c1Alive = creature1.getHealth() > 0;
        Creature winner = c1Alive ? creature1 : creature2;
        Creature loser = c1Alive ? creature2 : creature1;
        System.out.println(">> " + winner.getName() + " defeated " + loser.getName() + "!");
    }

    private void performStrike(Creature attacker, String attackerArmy, Creature defender, String defenderArmy) {
        int damage = attacker.getDamage();
        int newHealth = defender.getHealth() - damage;
        if (newHealth < Constants.MIN_VALID_HEALTH) {
            newHealth = Constants.MIN_VALID_HEALTH;
        }
        defender.setHealth(newHealth);

        System.out.println(String.format("%-25s %8d  %-8s %-25s %18d  %-8s", attacker.getName(), damage, attackerArmy, defender.getName(), defender.getHealth(), defenderArmy));
    }

    private void announceWinner() {
        int total1 = army1.getTotalHealth();
        int total2 = army2.getTotalHealth();
        String resultMessage;

        if (total1 > total2) {
            resultMessage = ">>> " + Constants.ARMY1_NAME + " wins the battle! <<<";
        }
        else if (total2 > total1) {
            resultMessage = ">>> " + Constants.ARMY2_NAME + " wins the battle! <<<";
        }
        else {
            resultMessage = ">>> The battle ends in a tie! <<<";
        }

        System.out.println("\n" + resultMessage + "\n" + Constants.ARMY1_NAME + " overall health: " + total1 + "\n" + Constants.ARMY2_NAME + " overall health: " + total2);
    }
}

public class Main {

    enum MenuOptions {
        INVALID,
        BATTLE,
        QUIT
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Game game = new Game();

        MenuOptions userChoice = MenuOptions.INVALID;
        boolean keepRunning = true;

        while (keepRunning) {
            printMenu();
            userChoice = readMenuChoice(input);

            switch (userChoice) {
                case BATTLE:
                    game.play(input);
                    break;
                case QUIT:
                    System.out.println("\nExiting battle application. Goodbye!");
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid menu selection. Please try again.");
            }
        }

        input.close();
    }

    private static void printMenu() {
        System.out.print("\n=== ARMIES BATTLE ARENA ===\n1. Battle\n2. Quit\nEnter choice: ");
    }

    private static MenuOptions readMenuChoice(Scanner input) {
        MenuOptions result = MenuOptions.INVALID;
        int choice = Constants.DUMMY_VALUE;

        if (input.hasNextInt()) {
            choice = input.nextInt();
        }
        input.nextLine();

        switch (choice) {
            case 1:
                result = MenuOptions.BATTLE;
                break;
            case 2:
                result = MenuOptions.QUIT;
                break;
            default:
                result = MenuOptions.INVALID;
        }

        return result;
    }
}

/*Output
/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=52951 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/aidentsang/IdeaProjects/CS213_L6_AT/out/production/CS213_L6_AT Main

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: !
Invalid menu selection. Please try again.

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: a
Invalid menu selection. Please try again.

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: 1

How many creatures per army (1-12)? 13
Invalid input. Please enter a number between 1 and 12.

How many creatures per army (1-12)? -1
Invalid input. Please enter a number between 1 and 12.

How many creatures per army (1-12)? !a
Invalid input. Please enter a number between 1 and 12.

How many creatures per army (1-12)? Invalid input. Please enter a number between 1 and 12.

How many creatures per army (1-12)? 12

########## NEW BATTLE ##########

Army 1 Stats before the Battle
Creature        Type                   Strength     Health
Cassia          macara                       67         70
Chester         superbahamut                183         97
Hollis          bahamut                     194        162
Karan           superbahamut                181         99
Seren           macara                      128        165
Kaelith         cyberbahamut                116         87
Faelan          macara                       95        185
Lunara          superbahamut                173        193
Ragnar          cyberbahamut                142        195
Lagnar          cyberbahamut                155        142
Clamar          cyberbahamut                171        182
Orrin           cyberbahamut                103        127
Total health of Army 1: 1704

Army 2 Stats before the Battle
Creature        Type                   Strength     Health
Theron          macara                       54         64
Thorfin         macara                      112        162
Petra           macara                      125         92
Quillon         bahamut                     154         55
Morwen          macara                      147        142
Dorian          bahamut                     124        175
Nyx             bahamut                      95         82
Scrandal        superbahamut                136        132
Cookie          superbahamut                 65         80
Aldric          bahamut                      67        102
Grisha          bahamut                     125         97
Isolde          cyberbahamut                188        111
Total health of Army 2: 1294

Attacker                    Damage  Army     Defender                   Defender's Health  Army    

--- Duel #1: Cassia the macara (Army 1) vs Theron the macara (Army 2) ---
Cassia the macara attacks first!
Cassia the macara               49  Army 1   Theron the macara                         15  Army 2  
Theron the macara               33  Army 2   Cassia the macara                         37  Army 1  
Cassia the macara               57  Army 1   Theron the macara                          0  Army 2  
>> Cassia the macara defeated Theron the macara!

--- Duel #2: Chester the superbahamut (Army 1) vs Thorfin the macara (Army 2) ---
Chester the superbahamut attacks first!
Chester the superbahamut        34  Army 1   Thorfin the macara                       128  Army 2  
Thorfin the macara             174  Army 2   Chester the superbahamut                   0  Army 1  
>> Thorfin the macara defeated Chester the superbahamut!

--- Duel #3: Hollis the bahamut (Army 1) vs Petra the macara (Army 2) ---
Petra the macara attacks first!
Petra the macara                36  Army 2   Hollis the bahamut                       126  Army 1  
Hollis the bahamut             175  Army 1   Petra the macara                           0  Army 2  
>> Hollis the bahamut defeated Petra the macara!

--- Duel #4: Karan the superbahamut (Army 1) vs Quillon the bahamut (Army 2) ---
Quillon the bahamut attacks first!
Quillon the bahamut             28  Army 2   Karan the superbahamut                    71  Army 1  
Karan the superbahamut          94  Army 1   Quillon the bahamut                        0  Army 2  
>> Karan the superbahamut defeated Quillon the bahamut!

--- Duel #5: Seren the macara (Army 1) vs Morwen the macara (Army 2) ---
Morwen the macara attacks first!
Morwen the macara               21  Army 2   Seren the macara                         144  Army 1  
Seren the macara                68  Army 1   Morwen the macara                         74  Army 2  
Morwen the macara              130  Army 2   Seren the macara                          14  Army 1  
Seren the macara               224  Army 1   Morwen the macara                          0  Army 2  
>> Seren the macara defeated Morwen the macara!

--- Duel #6: Kaelith the cyberbahamut (Army 1) vs Dorian the bahamut (Army 2) ---
Kaelith the cyberbahamut attacks first!
Kaelith the cyberbahamut       136  Army 1   Dorian the bahamut                        39  Army 2  
Dorian the bahamut              33  Army 2   Kaelith the cyberbahamut                  54  Army 1  
Kaelith the cyberbahamut        49  Army 1   Dorian the bahamut                         0  Army 2  
>> Kaelith the cyberbahamut defeated Dorian the bahamut!

--- Duel #7: Faelan the macara (Army 1) vs Nyx the bahamut (Army 2) ---
Nyx the bahamut attacks first!
Nyx the bahamut                 10  Army 2   Faelan the macara                        175  Army 1  
Faelan the macara              186  Army 1   Nyx the bahamut                            0  Army 2  
>> Faelan the macara defeated Nyx the bahamut!

--- Duel #8: Lunara the superbahamut (Army 1) vs Scrandal the superbahamut (Army 2) ---
Scrandal the superbahamut attacks first!
Scrandal the superbahamut      137  Army 2   Lunara the superbahamut                   56  Army 1  
Lunara the superbahamut        199  Army 1   Scrandal the superbahamut                  0  Army 2  
>> Lunara the superbahamut defeated Scrandal the superbahamut!

--- Duel #9: Ragnar the cyberbahamut (Army 1) vs Cookie the superbahamut (Army 2) ---
Ragnar the cyberbahamut attacks first!
Ragnar the cyberbahamut        129  Army 1   Cookie the superbahamut                    0  Army 2  
>> Ragnar the cyberbahamut defeated Cookie the superbahamut!

--- Duel #10: Lagnar the cyberbahamut (Army 1) vs Aldric the bahamut (Army 2) ---
Lagnar the cyberbahamut attacks first!
Lagnar the cyberbahamut         91  Army 1   Aldric the bahamut                        11  Army 2  
Aldric the bahamut              62  Army 2   Lagnar the cyberbahamut                   80  Army 1  
Lagnar the cyberbahamut         79  Army 1   Aldric the bahamut                         0  Army 2  
>> Lagnar the cyberbahamut defeated Aldric the bahamut!

--- Duel #11: Clamar the cyberbahamut (Army 1) vs Grisha the bahamut (Army 2) ---
Grisha the bahamut attacks first!
Grisha the bahamut              17  Army 2   Clamar the cyberbahamut                  165  Army 1  
Clamar the cyberbahamut         93  Army 1   Grisha the bahamut                         4  Army 2  
Grisha the bahamut              15  Army 2   Clamar the cyberbahamut                  150  Army 1  
Clamar the cyberbahamut         93  Army 1   Grisha the bahamut                         0  Army 2  
>> Clamar the cyberbahamut defeated Grisha the bahamut!

--- Duel #12: Orrin the cyberbahamut (Army 1) vs Isolde the cyberbahamut (Army 2) ---
Orrin the cyberbahamut attacks first!
Orrin the cyberbahamut           5  Army 1   Isolde the cyberbahamut                  106  Army 2  
Isolde the cyberbahamut         36  Army 2   Orrin the cyberbahamut                    91  Army 1  
Orrin the cyberbahamut          59  Army 1   Isolde the cyberbahamut                   47  Army 2  
Isolde the cyberbahamut         72  Army 2   Orrin the cyberbahamut                    19  Army 1  
Orrin the cyberbahamut          59  Army 1   Isolde the cyberbahamut                    0  Army 2  
>> Orrin the cyberbahamut defeated Isolde the cyberbahamut!

Army 1 Stats after the Battle
Creature        Type                   Strength     Health
Cassia          macara                       67         37
Chester         superbahamut                183          0
Hollis          bahamut                     194        126
Karan           superbahamut                181         71
Seren           macara                      128         14
Kaelith         cyberbahamut                116         54
Faelan          macara                       95        175
Lunara          superbahamut                173         56
Ragnar          cyberbahamut                142        195
Lagnar          cyberbahamut                155         80
Clamar          cyberbahamut                171        150
Orrin           cyberbahamut                103         19
Total health of Army 1: 977

Army 2 Stats after the Battle
Creature        Type                   Strength     Health
Theron          macara                       54          0
Thorfin         macara                      112        128
Petra           macara                      125          0
Quillon         bahamut                     154          0
Morwen          macara                      147          0
Dorian          bahamut                     124          0
Nyx             bahamut                      95          0
Scrandal        superbahamut                136          0
Cookie          superbahamut                 65          0
Aldric          bahamut                      67          0
Grisha          bahamut                     125          0
Isolde          cyberbahamut                188          0
Total health of Army 2: 128

>>> Army 1 wins the battle! <<<
Army 1 overall health: 977
Army 2 overall health: 128

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: 2

Exiting battle application. Goodbye!

Process finished with exit code 0*/
