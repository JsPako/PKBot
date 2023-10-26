package core.commands.gamble;

import java.util.Random;

public class highLow {

    private static int generate(){
        Random rand = new Random();
        return rand.nextInt(1,15);
    }
}
