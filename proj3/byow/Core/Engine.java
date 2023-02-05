package byow.Core;

import byow.Core.Objects.Player;
import byow.TileEngine.Tileset;
import byow.TileEngine.dTERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.util.Random;

import static byow.Core.Utils.readObject;
import static byow.Core.Utils.writeObject;
import static edu.princeton.cs.algs4.StdDraw.hasNextKeyTyped;
import static edu.princeton.cs.algs4.StdDraw.nextKeyTyped;

public class Engine {
    Dungeon dungeon;
    String gameDirectoryPath;
    dTERenderer ter = new dTERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height
            - (Toolkit.getDefaultToolkit().getScreenSize().height / 10);

    public Engine() {
        File gameDirectory = new File("game");
        gameDirectoryPath =  gameDirectory.getPath();
        if  (!gameDirectory.exists()) {
            gameDirectory.mkdirs();
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // display main menu with:
        // 1. New game (N)
        // 2. Load Game (L)
        // 3. Character (C)
        // 4. Quit (Q)

        // initialize canvas
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        // fonts
        Font fontBig = new Font("Monaco", Font.BOLD, HEIGHT / 8);
        Font fontSmall = new Font("Monaco", Font.PLAIN, HEIGHT / 15);

        //create text
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 5.0), "CS61B: THE GAME");
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.7), "New Game (N)");
        StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.5), "Load Game (L)");
        StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.345), "Character (C)");
        StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.24), "Quit (Q)");
        StdDraw.show();

        // initializes main menu and requests input
        boolean namingPlayer = false;
        TETile avatar = Tileset.AVATAR;
        String playerName = "Player 1";
        while (true) {
            // brings up character customization menu if c was previously selected
            if (namingPlayer) {
                StringBuilder name = new StringBuilder();
                while (namingPlayer) { // lets player change name
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setFont(fontBig);
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 5.0), "Enter a name:");
                    StdDraw.setFont(fontSmall);
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 2.0), String.valueOf(name));
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.5), "press ; to confirm");
                    StdDraw.show();
                    StdDraw.pause(100);
                    if (hasNextKeyTyped()) {
                        char c = nextKeyTyped();
                        if (c == ';') {
                            namingPlayer = false;
                        } else {
                            name.append(c);
                        }
                    }
                }
                boolean changingAvatar = true;
                while (changingAvatar) { // Lets player change avatar
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setFont(fontBig);
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 5.0), "Select a character:");
                    StdDraw.setFont(fontSmall);
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.7), "default @ (d)");
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.5), "grass (g)");
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.345), "flower (f)");
                    StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.24), "triangle (t)");
                    StdDraw.show();
                    StdDraw.pause(100);
                    if (hasNextKeyTyped()) {
                        char c = nextKeyTyped();
                        switch (c) {
                            case 'd', 'D' -> changingAvatar = false;
                            case 'g', 'G' -> {
                                avatar = Tileset.GRASS;
                                changingAvatar = false;
                            }
                            case 'f', 'F' -> {
                                avatar = Tileset.FLOWER;
                                changingAvatar = false;
                            }
                            case 't', 'T' -> {
                                avatar = Tileset.MOUNTAIN;
                                changingAvatar = false;
                            }
                            default -> {
                            }
                        }
                    }
                }
                playerName = name.toString();
                // back to main menu
                StdDraw.clear(Color.black);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 5.0), "CS61B: THE GAME");
                StdDraw.setFont(fontSmall);
                StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.7), "New Game (N)");
                StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.5), "Load Game (L)");
                StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.345), "Character (C)");
                StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.24), "Quit (Q)");
                StdDraw.textLeft(2, HEIGHT - (HEIGHT / 1.05), "Player Name: " + playerName);
                StdDraw.show();
            }
            if (hasNextKeyTyped()) {
                switch (nextKeyTyped()) {
                    case 'c':
                    case 'C':
                        namingPlayer = true;
                        break;
                    case 'n':// create new game
                    case 'N':
                        // request a seed
                        StringBuilder input = new StringBuilder();
                        while (true) {
                            StdDraw.clear(Color.BLACK);
                            StdDraw.setFont(fontBig);
                            StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 5.0), "Enter a seed:");
                            StdDraw.setFont(fontSmall);
                            StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 2.0), String.valueOf(input));
                            StdDraw.text(WIDTH / 2.0, HEIGHT - (HEIGHT / 1.5), "press S to confirm");
                            StdDraw.show();
                            StdDraw.pause(100);
                            if (hasNextKeyTyped()) {
                                char c = nextKeyTyped();
                                switch (c) {
                                    case '0':
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        input.append(c);
                                        break;
                                    case 'S':
                                    case 's':// generate world with string
                                        if (input.isEmpty()) {
                                            break;
                                        }
                                        // Use seed to generate a new dungeon
                                        long seed = Long.parseLong(input.toString());
                                        Random seed1 = new Random(seed);
                                        DungeonGenerator newWorld = new DungeonGenerator(seed1);
                                        newWorld.setMaximumX((HEIGHT / 20) - 15);
                                        newWorld.setMaximumY((WIDTH / 15) - 15);
                                        this.dungeon = newWorld.generateDungeon(seed1.nextInt(10, 15));
                                        TETile[][] map = dungeon.getMap();
                                        // initial setup for the game
                                        dungeon.getMyPlayer().name = playerName;
                                        dungeon.getMyPlayer().setAvatar(avatar);
                                        boolean gameOver = false;
                                        boolean colon = false;
                                        ter.initialize(map.length + 2, map[0].length + 1, 1, 1);
                                        while (!gameOver) {
                                            map = dungeon.getMap();
                                            ter.renderFrame(map, dungeon);
                                            updateHUD();
                                            if (hasNextKeyTyped()) {
                                                switch (nextKeyTyped()) {
                                                    case 'w':
                                                    case 'W':
                                                        dungeon.getMyPlayer().move(0, 1);
                                                        colon = false;
                                                        break;
                                                    case 's':
                                                        dungeon.getMyPlayer().move(0, -1);
                                                        colon = false;
                                                        break;
                                                    case 'd':
                                                        dungeon.getMyPlayer().move(1, 0);
                                                        colon = false;
                                                        break;
                                                    case 'a':
                                                        dungeon.getMyPlayer().move(-1, 0);
                                                        colon = false;
                                                        break;
                                                    case ':':
                                                        colon = true;
                                                        break;
                                                    case 'l':
                                                    case 'L':
                                                        dungeon.lightSwitch();
                                                        break;
                                                    case 'q':
                                                    case 'Q':
                                                        if (colon) {
                                                            saveGame();
                                                            gameOver = true;
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                if (dungeon.getMyPlayer().getHealth() <= 0) {
                                                    gameOver = true;
                                                }
                                            }
                                        }
                                        interactWithKeyboard();
                                        return;
                                    default:
                                        break;
                                }
                            }
                        }
                    case 'l':
                    case 'L':
                        dungeon = loadGame();
                        TETile[][] map = dungeon.getMap();
                        // initial setup for the game
                        boolean gameOver = false;
                        boolean colon = false;
                        ter.initialize(map.length + 2, map[0].length + 1, 1, 1);
                        while (!gameOver) {
                            map = dungeon.getMap();
                            ter.renderFrame(map, dungeon);
                            updateHUD();
                            if (hasNextKeyTyped()) {
                                switch (nextKeyTyped()) {
                                    case 'w':
                                    case 'W':
                                        dungeon.getMyPlayer().move(0, 1);
                                        colon = false;
                                        break;
                                    case 's':
                                        dungeon.getMyPlayer().move(0, -1);
                                        colon = false;
                                        break;
                                    case 'd':
                                        dungeon.getMyPlayer().move(1, 0);
                                        colon = false;
                                        break;
                                    case 'a':
                                        dungeon.getMyPlayer().move(-1, 0);
                                        colon = false;
                                        break;
                                    case ':':
                                        colon = true;
                                        break;
                                    case 'l':
                                    case 'L':
                                        dungeon.lightSwitch();
                                        break;
                                    case 'q':
                                    case 'Q':
                                        if (colon) {
                                            saveGame();
                                            gameOver = true;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                if (dungeon.getMyPlayer().getHealth() <= 0) {
                                    gameOver = true;
                                }
                            }
                        }
                        interactWithKeyboard();
                        return;
                    case 'q':
                    case 'Q':
                        return;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        String playerName = "Player 1";
        char[] inputs = input.toCharArray();
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == 'n' || inputs[i] == 'N') {
                input = input.substring(1);
                StringBuilder seedString = new StringBuilder();
                while (true) {
                    i++;
                    switch (inputs[i]) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> seedString.append(inputs[i]);
                        case 'S', 's' -> {// generate world with string
                            long seed = Long.parseLong(seedString.toString());
                            Random seed1 = new Random(seed);
                            DungeonGenerator newWorld = new DungeonGenerator(seed1);
                            Dungeon dungeon = newWorld.generateDungeon(seed1.nextInt(10, 15));
                            TETile[][] map = dungeon.getMap();
                            // initial setup for the game
                            dungeon.getMyPlayer().name = playerName;
                            boolean gameOver = false;
                            boolean colon = false;
                            ter.initialize(map.length + 2, map[0].length + 1, 1, 1);
                            while (!gameOver) {
                                i++;
                                try {
                                    char c = inputs[i];
                                } catch (Exception e) {
                                    return dungeon.getMap();
                                }
                                map = dungeon.getMap();
                                ter.renderFrame(map, dungeon);
                                switch (inputs[i]) {
                                    case 'w':
                                    case 'W':
                                        dungeon.getMyPlayer().move(0, 1);
                                        colon = false;
                                        break;
                                    case 'S':
                                    case 's':
                                        dungeon.getMyPlayer().move(0, -1);
                                        colon = false;
                                        break;
                                    case 'd':
                                    case 'D':
                                        dungeon.getMyPlayer().move(1, 0);
                                        colon = false;
                                        break;
                                    case 'a':
                                    case 'A':
                                        dungeon.getMyPlayer().move(-1, 0);
                                        colon = false;
                                        break;
                                    case ':':
                                        colon = true;
                                        break;
                                    case 'l':
                                    case 'L':
                                        dungeon.lightSwitch();
                                        break;
                                    case 'q':
                                    case 'Q':
                                        if (colon) {
                                            // saveGame();
                                            gameOver = true;
                                            saveGame();
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
            if (inputs[i] == 'l' || inputs[i] == 'L') {
                dungeon = loadGame();
                return dungeon.getMap();
            }
            if (inputs[i] == 'q' || inputs[i] == 'Q') {
                return null;
            }
        }
        return null;
    }

    private void init() {
        File gameDirectory = new File("game");
        this.gameDirectoryPath = gameDirectory.getPath();
        if (!gameDirectory.exists()) {
            gameDirectory.mkdir();
        }
    }
    private void updateHUD() {
        // draws the HUD
        TETile[][] world = dungeon.getMap();
        String displayText;
        int mouseX = (int) StdDraw.mouseX() - 1;
        int mouseY = (int) StdDraw.mouseY() - 1;
        try {
            TETile tile = world[mouseX][mouseY];
        }
        catch (Exception e) {
            displayText = "Off Screen";
            StdDraw.text(world.length / 2.0, 0.4, displayText);
            StdDraw.show();
            return;
        }
        TETile tile = world[mouseX][mouseY];
        if (tile == Tileset.NOTHING) {
            displayText = "nothing";
        } else if (tile == Tileset.FLOOR) {
            displayText = "floor";
        } else if (tile == Tileset.WALL) {
            displayText = "wall";
        } else {
            displayText = "avatar";
        }
        StdDraw.text(world.length / 2.0, 0.4, displayText);
        StdDraw.show();
    }

    public void saveGame() {
        TETile[][] world = dungeon.getMap();
        char[][] charWorld = new char[world.length][world[0].length];
        for (int y = world[0].length - 1;  y >= 0; y--) {
            for (int x = 0;  x < world.length; x++) {
                char ch = mapTETileToChar(world[x][y]);
                charWorld[x][y] = ch;
            }
        }
        File file = new File(gameDirectoryPath + "/world.txt");
        writeObject(file, charWorld);

        file = new File(gameDirectoryPath + "/currentLocation.txt");
        writeObject(file, new Point(dungeon.getMyPlayer().getxCoord(), dungeon.getMyPlayer().getyCoord()));

        file = new File(gameDirectoryPath + "/mapSize.txt");
        writeObject(file, new Point(world.length, world[0].length));

        file = new File(gameDirectoryPath + "/Health.txt");
        writeObject(file, dungeon.getMyPlayer().getHealth());

        file = new File(gameDirectoryPath + "/playerName.txt");
        writeObject(file, dungeon.getMyPlayer().name);

        file = new File(gameDirectoryPath + "/playerAvatar.txt");
        writeObject(file, mapTETileToChar(dungeon.getMyPlayer().getAvatar()));
    }
    public Dungeon loadGame() {
        File file = new File(gameDirectoryPath + "/currentLocation.txt");
        Point avatarLocation = readObject(file, Point.class);

        file = new File(gameDirectoryPath + "/mapSize.txt");
        Point mapSize = readObject(file, Point.class);
        TETile[][] world = new TETile[mapSize.x][mapSize.y];

        file = new File(gameDirectoryPath + "/Health.txt");
        int health = readObject(file, Integer.class);

        file = new File(gameDirectoryPath + "/playerName.txt");
        String playerName = readObject(file, String.class);

        file = new File(gameDirectoryPath + "/playerAvatar.txt");
        char character = readObject(file, Character.class);
        TETile Avatar = mapCharToTETile(character);

        file = new File(gameDirectoryPath + "/world.txt");
        char[][] charWorld = readObject(file, char[][].class);

        for (int y = world[0].length - 1;  y >= 0; y--) {
            for (int x = 0;  x < world.length; x++) {
                TETile tile = mapCharToTETile(charWorld[x][y]);
                world[x][y] = tile;
            }
        }
        Player player = new Player(avatarLocation.x, avatarLocation.y, health, world);
        player.name = playerName;
        player.setAvatar(Avatar);
        return new Dungeon(world, player);

    }
    private char mapTETileToChar(TETile tile) {
        if (tile == Tileset.NOTHING){
            return ' ';
        } else if (tile == Tileset.FLOOR){
            return '.';
        } else if (tile == Tileset.WALL){
            return '#';
        } else if (tile == Tileset.MOUNTAIN){
            return '▲';
        } else if (tile == Tileset.FLOWER) {
            return '❀';
        } else if (tile == Tileset.GRASS) {
            return '≈';
        } else {
            return '@';
        }
    }

    private TETile mapCharToTETile(char c) {
        if (c == ' '){
            return Tileset.NOTHING;
        } else if (c == '.'){
            return Tileset.FLOOR;
        } else if (c == '#'){
            return Tileset.WALL;
        } else if (c == '▲'){
            return Tileset.MOUNTAIN;
        } else if (c == '❀') {
            return Tileset.FLOWER;
        } else if (c == '≈') {
            return Tileset.GRASS;
        } else {
            return Tileset.AVATAR;
        }
    }
}
