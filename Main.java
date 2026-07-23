// Aiden Tsang
// CS213 Lab 6

import java.util.Scanner;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;

public class Main {
    static final String ARMY1_FILE = "in_army1names.txt";
    static final String ARMY2_FILE = "in_army2names.txt";
    static final String OUTPUT_FILE = "out_battle_log.txt";

    enum MenuOptions { INVALID, BATTLE, QUIT }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Game game = new Game();
        PrintWriter fileOut = null;

        try {
            fileOut = new PrintWriter(new java.io.FileWriter(OUTPUT_FILE, true));
        }
        catch (java.io.IOException error) {
            System.out.println("Could not open " + OUTPUT_FILE);
        }

        MenuOptions userChoice = MenuOptions.INVALID;
        boolean keepRunning = true;

        while (keepRunning) {
            printMenu();
            userChoice = readMenuChoice(input);

            switch (userChoice) {
                case BATTLE:
                    if (fileOut != null) {
                        game.play(input, fileOut);
                    }
                    else {
                        System.out.println("Output file not available");
                    }
                    break;
                case QUIT:
                    System.out.println("\nExiting battle application. Goodbye!");
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid menu selection. Please try again.");
            }
        }

        if (fileOut != null) {
            fileOut.close();
        }
        input.close();
    }

    public static String findFile(String fileName) {
        File inSrcFolder = new File("src/" + fileName);
        String result = (inSrcFolder.exists()) ? ("src/" + fileName) : fileName;
        return result;
    }

    private static void printMenu() {
        System.out.println("\n=== ARMIES BATTLE ARENA ===");
        System.out.println("1. Battle");
        System.out.println("2. Quit");
        System.out.print("Enter choice: ");
    }

    private static MenuOptions readMenuChoice(Scanner input) {
        int choice = Constants.DUMMY_VALUE;
        if (input.hasNextInt()) {
            choice = input.nextInt();
        }
        input.nextLine();

        switch (choice) {
            case 1:
                return MenuOptions.BATTLE;
            case 2:
                return MenuOptions.QUIT;
            default:
                return MenuOptions.INVALID;
        }
    }
}

interface Constants {
    int MIN_STAT = 50;
    int MAX_STAT = 197;
    int STAT_RANGE = MAX_STAT - MIN_STAT + 1;
    int MAX_ARMY_SIZE = 15;
    int MAX_BATTLE_SIZE = 15;
    int MIN_ARMY_SIZE = 1;
    int DEFAULT_ARMY_SIZE = 0;
    int DUMMY_VALUE = -1;
    String DEFAULT_NAME = "n/a";
    String DEFAULT_ARMY_NAME = "Unnamed";
    int BAHAMUT_BONUS_DAMAGE = 25;
    int BAHAMUT_BONUS_CHANCE = 10;
    int PERCENT_ROLL = 100;
    int MACARA_ROLL = 5;
    int MACARA_MULTIPLIER = 2;
    String ARMY1_NAME = "Army 1";
    String ARMY2_NAME = "Army 2";
}

enum CreatureType {
    BAHAMUT(1, "bahamut"),
    MACARA(2, "macara"),
    CYBERBAHAMUT(3, "cyberbahamut"),
    SUPERBAHAMUT(4, "superbahamut");

    private final int value;
    private final String description;

    CreatureType(int newValue, String newDescription) {
        value = newValue;
        description = newDescription;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}

abstract class Creature implements Constants {
    protected String name = DEFAULT_NAME;
    protected int health = MIN_STAT;
    protected int strength = MIN_STAT;
    private static final Random rand = new Random();

    public Creature() {
        setCreature(DEFAULT_NAME, MIN_STAT, MIN_STAT);
    }

    public Creature(String newName, int newHealth, int newStrength) {
        setCreature(newName, newHealth, newStrength);
    }

    public void setCreature(String newName, int newHealth, int newStrength) {
        if (newHealth < 0 || newHealth > Constants.MAX_STAT || newStrength < Constants.MIN_STAT || newStrength > Constants.MAX_STAT) {
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
    private static final Random rand = new Random();

    public Bahamut() {
        super();
    }

    public Bahamut(String newName, int newHealth, int newStrength) {
        super(newName, newHealth, newStrength);
    }

    public String getCreatureType() {
        return "bahamut";
    }

    public int getDamage() {
        int damage = super.getDamage();
        if (rand.nextInt(PERCENT_ROLL) < BAHAMUT_BONUS_CHANCE) {
            damage = damage + BAHAMUT_BONUS_DAMAGE;
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
        return "cyberbahamut";
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
        return "superbahamut";
    }
}

class Macara extends Creature {
    private static final Random rand = new Random();

    public Macara() {
        super();
    }

    public Macara(String newName, int newHealth, int newStrength) {
        super(newName, newHealth, newStrength);
    }

    public String getCreatureType() {
        return "macara";
    }

    public int getDamage() {
        int damage = super.getDamage();
        if (rand.nextInt(MACARA_ROLL) == 0) {
            damage = damage * MACARA_MULTIPLIER;
        }
        return damage;
    }
}

class Army implements Constants {
    private String armyName = DEFAULT_ARMY_NAME;
    private int size = DEFAULT_ARMY_SIZE;
    private Creature[] creatures = new Creature[MAX_ARMY_SIZE];
    private boolean[] usedNames;
    private String[] creatureNames = new String[0];
    private static final Random rand = new Random();

    public Army() {
        armyName = DEFAULT_ARMY_NAME;
        size = DEFAULT_ARMY_SIZE;
        for (int i = 0; i < MAX_ARMY_SIZE; i++) {
            creatures[i] = new Macara();
        }
    }

    public Army(String newArmyName) {
        this();
        armyName = newArmyName;
    }

    public void createArmy(String newArmyName, int newSize, String[] namesFromFile) {
        if (newSize < MIN_ARMY_SIZE || newSize > MAX_BATTLE_SIZE) {
            System.out.println("Invalid army size; keeping current values");
            return;
        }
        armyName = newArmyName;
        size = newSize;
        creatureNames = namesFromFile;
        usedNames = new boolean[namesFromFile.length];
        resetUsedNames();
        for (int i = 0; i < size; i++) {
            creatures[i] = createRandomCreature();
        }
    }

    private void resetUsedNames() {
        for (int i = 0; i < usedNames.length; i++) {
            usedNames[i] = false;
        }
    }

    private Creature createRandomCreature() {
        int creatureType = rand.nextInt(4);
        String uniqueName = getUniqueName();
        int health = MIN_STAT + rand.nextInt(STAT_RANGE);
        int strength = MIN_STAT + rand.nextInt(STAT_RANGE);

        switch (creatureType) {
            case 0:
                return new Bahamut(uniqueName, health, strength);
            case 1:
                return new Cyberbahamut(uniqueName, health, strength);
            case 2:
                return new Superbahamut(uniqueName, health, strength);
            case 3:
                return new Macara(uniqueName, health, strength);
            default:
                return new Macara(uniqueName, health, strength);
        }
    }

    private boolean areAllNamesUsed() {
        for (boolean used : usedNames) {
            if (!used) {
                return false;
            }
        }
        return true;
    }

    private String getUniqueName() {
        if (creatureNames.length == 0 || areAllNamesUsed()) {
            return "Warrior_" + (rand.nextInt(1000) + 1);
        }

        int index;
        do {
            index = rand.nextInt(creatureNames.length);
        } while (usedNames[index]);

        usedNames[index] = true;
        return creatureNames[index];
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
        for (int i = 0; i < MAX_ARMY_SIZE; i++) {
            creatures[i].setCreature(DEFAULT_NAME, MIN_STAT, MIN_STAT);
        }
        armyName = DEFAULT_ARMY_NAME;
        size = DEFAULT_ARMY_SIZE;
    }

    public void printStats(String label) {
        System.out.println("\n" + armyName + " Stats " + label);
        System.out.println(String.format("%-15s %-20s %10s %10s", "Creature", "Type", "Strength", "Health"));
        for (int i = 0; i < size; i++) {
            System.out.println(creatures[i].toString());
        }
        System.out.println("Total health of " + armyName + ": " + getTotalHealth());
    }

    public void printStats(PrintWriter fileOut, String label) {
        String header = "\n" + armyName + " Stats " + label;
        String columnHeader = String.format("%-15s %-20s %10s %10s", "Creature", "Type", "Strength", "Health");
        System.out.println(header);
        System.out.println(columnHeader);
        if (fileOut != null) {
            fileOut.println(header);
            fileOut.println(columnHeader);
        }
        for (int i = 0; i < size; i++) {
            String line = creatures[i].toString();
            System.out.println(line);
            if (fileOut != null) {
                fileOut.println(line);
            }
        }
        String totalLine = "Total health of " + armyName + ": " + getTotalHealth();
        System.out.println(totalLine);
        if (fileOut != null) {
            fileOut.println(totalLine);
        }
    }
}

class Game implements Constants {
    private Army army1 = new Army(ARMY1_NAME);
    private Army army2 = new Army(ARMY2_NAME);
    private Random rand = new Random();

    public void play(Scanner input, PrintWriter fileOut) {
        String[] army1Names = readNamesFromFile(Main.ARMY1_FILE);
        String[] army2Names = readNamesFromFile(Main.ARMY2_FILE);

        int armySize = askArmySize(input);
        army1.createArmy(ARMY1_NAME, armySize, army1Names);
        army2.createArmy(ARMY2_NAME, armySize, army2Names);

        writeLine(fileOut, "\n########## NEW BATTLE ##########");
        army1.printStats(fileOut, "before the Battle");
        army2.printStats(fileOut, "before the Battle");

        writeLine(fileOut, "\n" + String.format("%-25s %8s  %-8s %-25s %18s  %-8s", "Attacker", "Damage", "Army", "Defender", "Defender's Health", "Army"));

        int pairIndex = 0;
        while (pairIndex < army1.getSize() && pairIndex < army2.getSize()) {
            runDuel(army1.getCreature(pairIndex), army2.getCreature(pairIndex), fileOut, pairIndex + 1);
            pairIndex++;
        }

        army1.printStats(fileOut, "after the Battle");
        army2.printStats(fileOut, "after the Battle");
        announceWinner(fileOut);

        army1.reset();
        army2.reset();
    }

    private void writeLine(PrintWriter fileOut, String line) {
        System.out.println(line);
        if (fileOut != null) {
            fileOut.println(line);
        }
    }

    private int askArmySize(Scanner input) {
        int size = DUMMY_VALUE;
        while (size < MIN_ARMY_SIZE || size > MAX_BATTLE_SIZE) {
            System.out.print("\nHow many creatures per army (1-15)? ");
            if (input.hasNextInt()) {
                size = input.nextInt();
            }
            else {
                input.nextLine();
                size = DUMMY_VALUE;
            }
            if (size < MIN_ARMY_SIZE || size > MAX_BATTLE_SIZE) {
                System.out.println("Invalid input. Please enter a number between 1 and 15.");
            }
        }
        input.nextLine();
        return size;
    }

    private void runDuel(Creature creature1, Creature creature2, PrintWriter fileOut, int duelNumber) {
        Creature attacker = creature1;
        Creature defender = creature2;
        String attackerArmy = ARMY1_NAME;
        String defenderArmy = ARMY2_NAME;

        if (rand.nextInt(2) == 1) {
            attacker = creature2;
            defender = creature1;
            attackerArmy = ARMY2_NAME;
            defenderArmy = ARMY1_NAME;
        }

        writeLine(fileOut, "\n--- Duel #" + duelNumber + ": " + creature1.getName() + " (" + ARMY1_NAME + ") vs " + creature2.getName() + " (" + ARMY2_NAME + ") ---");
        writeLine(fileOut, attacker.getName() + " attacks first!");

        while (creature1.getHealth() > 0 && creature2.getHealth() > 0) {
            performStrike(attacker, attackerArmy, defender, defenderArmy, fileOut);

            if (attacker instanceof Superbahamut && defender.getHealth() > 0) {
                performStrike(attacker, attackerArmy, defender, defenderArmy, fileOut);
            }

            Creature tempCreature = attacker;
            attacker = defender;
            defender = tempCreature;
            String tempArmy = attackerArmy;
            attackerArmy = defenderArmy;
            defenderArmy = tempArmy;
        }

        Creature winner = (creature1.getHealth() > 0) ? creature1 : creature2;
        Creature loser = (creature1.getHealth() > 0) ? creature2 : creature1;
        writeLine(fileOut, ">> " + winner.getName() + " defeated " + loser.getName() + "!");
    }

    private void performStrike(Creature attacker, String attackerArmy, Creature defender, String defenderArmy, PrintWriter fileOut) {
        int damage = attacker.getDamage();
        int newHealth = defender.getHealth() - damage;
        if (newHealth < 0) {
            newHealth = 0;
        }
        defender.setHealth(newHealth);

        writeLine(fileOut, String.format("%-25s %8d  %-8s %-25s %18d  %-8s", attacker.getName(), damage, attackerArmy, defender.getName(), defender.getHealth(), defenderArmy));
    }

    private String[] readNamesFromFile(String fileName) {
        String[] temp = new String[24];
        int count = 0;
        try {
            String filePath = Main.findFile(fileName);
            Scanner fileIn = new Scanner(new File(filePath));
            while (fileIn.hasNextLine() && count < 24) {
                String line = fileIn.nextLine().trim();
                if (!line.isEmpty()) {
                    temp[count] = line;
                    count++;
                }
            }
            fileIn.close();
        }
        catch (java.io.FileNotFoundException error) {
            System.out.println("File " + fileName + " not found");
        }

        String[] names = new String[count];
        System.arraycopy(temp, 0, names, 0, count);
        return names;
    }

    private void announceWinner(PrintWriter fileOut) {
        int total1 = army1.getTotalHealth();
        int total2 = army2.getTotalHealth();
        String resultMessage;

        if (total1 > total2) {
            resultMessage = ">>> " + ARMY1_NAME + " wins the battle! <<<";
        }
        else if (total2 > total1) {
            resultMessage = ">>> " + ARMY2_NAME + " wins the battle! <<<";
        }
        else {
            resultMessage = ">>> The battle ends in a tie! <<<";
        }

        writeLine(fileOut, "\n" + resultMessage);
        writeLine(fileOut, ARMY1_NAME + " overall health: " + total1);
        writeLine(fileOut, ARMY2_NAME + " overall health: " + total2);
    }
}

/*Output
/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=49560 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/aidentsang/IdeaProjects/CS213_L6_AT/out/production/CS213_L6_AT Main

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: 1

How many creatures per army (1-15)? 15

########## NEW BATTLE ##########

Army 1 Stats before the Battle
Creature        Type                   Strength     Health
Theron          superbahamut                 65         90
Thorfin         cyberbahamut                114        125
Petra           bahamut                      96        193
Karan           superbahamut                186        179
Seren           macara                      141        165
Lunara          superbahamut                 69        148
Lagnar          cyberbahamut                189        154
Orrin           cyberbahamut                169         81
Quillon         bahamut                     170        136
Morwen          bahamut                     168         61
Nyx             superbahamut                146        163
Chester         bahamut                     125        175
Ragnar          bahamut                     192        138
Kaelith         superbahamut                 76         50
Cookie          cyberbahamut                 56        193
Total health of Army 1: 2051

Army 2 Stats before the Battle
Creature        Type                   Strength     Health
Aldric          cyberbahamut                148         79
Grisha          bahamut                     155        169
Isolde          bahamut                      63        104
Scrandal        macara                       84         72
Dorian          macara                      183        185
Hollis          cyberbahamut                122         56
Faelan          superbahamut                173         51
Clamar          superbahamut                 77        117
Cassia          macara                       70         51
Jorvik          bahamut                     184        137
Angel           superbahamut                190        123
Elowen          bahamut                     185        154
Brynn           bahamut                     196         57
Roshar          bahamut                     188        148
Froster         macara                      114        112
Total health of Army 2: 1615

Attacker                    Damage  Army     Defender                   Defender's Health  Army

--- Duel #1: Theron the superbahamut (Army 1) vs Aldric the cyberbahamut (Army 2) ---
Aldric the cyberbahamut attacks first!
Aldric the cyberbahamut         60  Army 2   Theron the superbahamut                   30  Army 1
Theron the superbahamut         16  Army 1   Aldric the cyberbahamut                   63  Army 2
Theron the superbahamut         50  Army 1   Aldric the cyberbahamut                   13  Army 2
Aldric the cyberbahamut        131  Army 2   Theron the superbahamut                    0  Army 1
>> Aldric the cyberbahamut defeated Theron the superbahamut!

--- Duel #2: Thorfin the cyberbahamut (Army 1) vs Grisha the bahamut (Army 2) ---
Grisha the bahamut attacks first!
Grisha the bahamut              56  Army 2   Thorfin the cyberbahamut                  69  Army 1
Thorfin the cyberbahamut        47  Army 1   Grisha the bahamut                       122  Army 2
Grisha the bahamut              37  Army 2   Thorfin the cyberbahamut                  32  Army 1
Thorfin the cyberbahamut        54  Army 1   Grisha the bahamut                        68  Army 2
Grisha the bahamut             152  Army 2   Thorfin the cyberbahamut                   0  Army 1
>> Grisha the bahamut defeated Thorfin the cyberbahamut!

--- Duel #3: Petra the bahamut (Army 1) vs Isolde the bahamut (Army 2) ---
Isolde the bahamut attacks first!
Isolde the bahamut               6  Army 2   Petra the bahamut                        187  Army 1
Petra the bahamut               41  Army 1   Isolde the bahamut                        63  Army 2
Isolde the bahamut              48  Army 2   Petra the bahamut                        139  Army 1
Petra the bahamut               55  Army 1   Isolde the bahamut                         8  Army 2
Isolde the bahamut              21  Army 2   Petra the bahamut                        118  Army 1
Petra the bahamut               76  Army 1   Isolde the bahamut                         0  Army 2
>> Petra the bahamut defeated Isolde the bahamut!

--- Duel #4: Karan the superbahamut (Army 1) vs Scrandal the macara (Army 2) ---
Scrandal the macara attacks first!
Scrandal the macara             71  Army 2   Karan the superbahamut                   108  Army 1
Karan the superbahamut          58  Army 1   Scrandal the macara                       14  Army 2
Karan the superbahamut         156  Army 1   Scrandal the macara                        0  Army 2
>> Karan the superbahamut defeated Scrandal the macara!

--- Duel #5: Seren the macara (Army 1) vs Dorian the macara (Army 2) ---
Seren the macara attacks first!
Seren the macara                99  Army 1   Dorian the macara                         86  Army 2
Dorian the macara              204  Army 2   Seren the macara                           0  Army 1
>> Dorian the macara defeated Seren the macara!

--- Duel #6: Lunara the superbahamut (Army 1) vs Hollis the cyberbahamut (Army 2) ---
Lunara the superbahamut attacks first!
Lunara the superbahamut         27  Army 1   Hollis the cyberbahamut                   29  Army 2
Lunara the superbahamut         70  Army 1   Hollis the cyberbahamut                    0  Army 2
>> Lunara the superbahamut defeated Hollis the cyberbahamut!

--- Duel #7: Lagnar the cyberbahamut (Army 1) vs Faelan the superbahamut (Army 2) ---
Lagnar the cyberbahamut attacks first!
Lagnar the cyberbahamut        118  Army 1   Faelan the superbahamut                    0  Army 2
>> Lagnar the cyberbahamut defeated Faelan the superbahamut!

--- Duel #8: Orrin the cyberbahamut (Army 1) vs Clamar the superbahamut (Army 2) ---
Orrin the cyberbahamut attacks first!
Orrin the cyberbahamut          51  Army 1   Clamar the superbahamut                   66  Army 2
Clamar the superbahamut         15  Army 2   Orrin the cyberbahamut                    66  Army 1
Clamar the superbahamut         42  Army 2   Orrin the cyberbahamut                    24  Army 1
Orrin the cyberbahamut         120  Army 1   Clamar the superbahamut                    0  Army 2
>> Orrin the cyberbahamut defeated Clamar the superbahamut!

--- Duel #9: Quillon the bahamut (Army 1) vs Cassia the macara (Army 2) ---
Quillon the bahamut attacks first!
Quillon the bahamut             87  Army 1   Cassia the macara                          0  Army 2
>> Quillon the bahamut defeated Cassia the macara!

--- Duel #10: Morwen the bahamut (Army 1) vs Jorvik the bahamut (Army 2) ---
Morwen the bahamut attacks first!
Morwen the bahamut             157  Army 1   Jorvik the bahamut                         0  Army 2
>> Morwen the bahamut defeated Jorvik the bahamut!

--- Duel #11: Nyx the superbahamut (Army 1) vs Angel the superbahamut (Army 2) ---
Angel the superbahamut attacks first!
Angel the superbahamut           5  Army 2   Nyx the superbahamut                     158  Army 1
Angel the superbahamut         141  Army 2   Nyx the superbahamut                      17  Army 1
Nyx the superbahamut           130  Army 1   Angel the superbahamut                     0  Army 2
>> Nyx the superbahamut defeated Angel the superbahamut!

--- Duel #12: Chester the bahamut (Army 1) vs Elowen the bahamut (Army 2) ---
Elowen the bahamut attacks first!
Elowen the bahamut              79  Army 2   Chester the bahamut                       96  Army 1
Chester the bahamut             92  Army 1   Elowen the bahamut                        62  Army 2
Elowen the bahamut             158  Army 2   Chester the bahamut                        0  Army 1
>> Elowen the bahamut defeated Chester the bahamut!

--- Duel #13: Ragnar the bahamut (Army 1) vs Brynn the bahamut (Army 2) ---
Ragnar the bahamut attacks first!
Ragnar the bahamut             154  Army 1   Brynn the bahamut                          0  Army 2
>> Ragnar the bahamut defeated Brynn the bahamut!

--- Duel #14: Kaelith the superbahamut (Army 1) vs Roshar the bahamut (Army 2) ---
Kaelith the superbahamut attacks first!
Kaelith the superbahamut        30  Army 1   Roshar the bahamut                       118  Army 2
Kaelith the superbahamut        18  Army 1   Roshar the bahamut                       100  Army 2
Roshar the bahamut             100  Army 2   Kaelith the superbahamut                   0  Army 1
>> Roshar the bahamut defeated Kaelith the superbahamut!

--- Duel #15: Cookie the cyberbahamut (Army 1) vs Froster the macara (Army 2) ---
Cookie the cyberbahamut attacks first!
Cookie the cyberbahamut          7  Army 1   Froster the macara                       105  Army 2
Froster the macara              63  Army 2   Cookie the cyberbahamut                  130  Army 1
Cookie the cyberbahamut         13  Army 1   Froster the macara                        92  Army 2
Froster the macara              54  Army 2   Cookie the cyberbahamut                   76  Army 1
Cookie the cyberbahamut         10  Army 1   Froster the macara                        82  Army 2
Froster the macara              43  Army 2   Cookie the cyberbahamut                   33  Army 1
Cookie the cyberbahamut         10  Army 1   Froster the macara                        72  Army 2
Froster the macara              55  Army 2   Cookie the cyberbahamut                    0  Army 1
>> Froster the macara defeated Cookie the cyberbahamut!

Army 1 Stats after the Battle
Creature        Type                   Strength     Health
Theron          superbahamut                 65          0
Thorfin         cyberbahamut                114          0
Petra           bahamut                      96        118
Karan           superbahamut                186        108
Seren           macara                      141          0
Lunara          superbahamut                 69        148
Lagnar          cyberbahamut                189        154
Orrin           cyberbahamut                169         24
Quillon         bahamut                     170        136
Morwen          bahamut                     168         61
Nyx             superbahamut                146         17
Chester         bahamut                     125          0
Ragnar          bahamut                     192        138
Kaelith         superbahamut                 76          0
Cookie          cyberbahamut                 56          0
Total health of Army 1: 904

Army 2 Stats after the Battle
Creature        Type                   Strength     Health
Aldric          cyberbahamut                148         13
Grisha          bahamut                     155         68
Isolde          bahamut                      63          0
Scrandal        macara                       84          0
Dorian          macara                      183         86
Hollis          cyberbahamut                122          0
Faelan          superbahamut                173          0
Clamar          superbahamut                 77          0
Cassia          macara                       70          0
Jorvik          bahamut                     184          0
Angel           superbahamut                190          0
Elowen          bahamut                     185         62
Brynn           bahamut                     196          0
Roshar          bahamut                     188        100
Froster         macara                      114         72
Total health of Army 2: 401

>>> Army 1 wins the battle! <<<
Army 1 overall health: 904
Army 2 overall health: 401

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: -1
Invalid menu selection. Please try again.

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: !a
Invalid menu selection. Please try again.

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: 1

How many creatures per army (1-15)? -1
Invalid input. Please enter a number between 1 and 15.

How many creatures per army (1-15)? !a
Invalid input. Please enter a number between 1 and 15.

How many creatures per army (1-15)? Invalid input. Please enter a number between 1 and 15.

How many creatures per army (1-15)? 3

########## NEW BATTLE ##########

Army 1 Stats before the Battle
Creature        Type                   Strength     Health
Lagnar          macara                      102        165
Cookie          superbahamut                125         65
Nyx             bahamut                     133        122
Total health of Army 1: 352

Army 2 Stats before the Battle
Creature        Type                   Strength     Health
Angel           superbahamut                 56         95
Cassia          cyberbahamut                 90        172
Elowen          cyberbahamut                137        117
Total health of Army 2: 384

Attacker                    Damage  Army     Defender                   Defender's Health  Army

--- Duel #1: Lagnar the macara (Army 1) vs Angel the superbahamut (Army 2) ---
Lagnar the macara attacks first!
Lagnar the macara              140  Army 1   Angel the superbahamut                     0  Army 2
>> Lagnar the macara defeated Angel the superbahamut!

--- Duel #2: Cookie the superbahamut (Army 1) vs Cassia the cyberbahamut (Army 2) ---
Cookie the superbahamut attacks first!
Cookie the superbahamut        125  Army 1   Cassia the cyberbahamut                   47  Army 2
Cookie the superbahamut         96  Army 1   Cassia the cyberbahamut                    0  Army 2
>> Cookie the superbahamut defeated Cassia the cyberbahamut!

--- Duel #3: Nyx the bahamut (Army 1) vs Elowen the cyberbahamut (Army 2) ---
Elowen the cyberbahamut attacks first!
Elowen the cyberbahamut        114  Army 2   Nyx the bahamut                            8  Army 1
Nyx the bahamut                 13  Army 1   Elowen the cyberbahamut                  104  Army 2
Elowen the cyberbahamut         66  Army 2   Nyx the bahamut                            0  Army 1
>> Elowen the cyberbahamut defeated Nyx the bahamut!

Army 1 Stats after the Battle
Creature        Type                   Strength     Health
Lagnar          macara                      102        165
Cookie          superbahamut                125         65
Nyx             bahamut                     133          0
Total health of Army 1: 230

Army 2 Stats after the Battle
Creature        Type                   Strength     Health
Angel           superbahamut                 56          0
Cassia          cyberbahamut                 90          0
Elowen          cyberbahamut                137        104
Total health of Army 2: 104

>>> Army 1 wins the battle! <<<
Army 1 overall health: 230
Army 2 overall health: 104

=== ARMIES BATTLE ARENA ===
1. Battle
2. Quit
Enter choice: 2

Exiting battle application. Goodbye!

Process finished with exit code 0
*/
