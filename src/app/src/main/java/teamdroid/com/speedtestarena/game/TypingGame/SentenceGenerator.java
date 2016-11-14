package teamdroid.com.speedtestarena.game.TypingGame;

/**
 * Created by Alan on 2016-10-26.
 */

import java.util.Random;

public class SentenceGenerator {

    // These variables must be static
    final static int NUMOFWORDS = 5;
    final static String SPACE = " ";
    final static String PERIOD = ".";

    static Random rand = new Random();

    private String artWords[] = { "a", "can", "I", "go", "get", "was", "we"}; // Article words
    private String adjWords[] = { "happy","kind","honest","polite","romantic"}; // Adjectives words
    private String nounWords[] = { "boy", "girl", "cat", "bus", "plane" }; // Noun words
    private String verbWords[] = { "drove", "jumped", "ran", "walked", "hopped" }; // Verb words
    private String prepositionWords[] = { "to", "from", "over", "under", "on" }; // Preposition words

    public String sentenceGenerator(int words ){
        String sentence;

        /* Generate sentence for level easy */
        if (words == 1) {
            sentence = nounWords[rand()];
            char c = sentence.charAt(0);
            sentence = sentence.replace(c, Character.toUpperCase(c));
            sentence += SPACE + (verbWords[rand()] + SPACE + prepositionWords[rand()]);
            sentence += SPACE + nounWords[rand()];
            sentence += PERIOD;
          /* Generate sentence for level medium */
        } else if (words == 2){
            sentence = artWords[rand()];
            char c = sentence.charAt(0);
            sentence = sentence.replace(c, Character.toUpperCase(c));
            sentence += SPACE + adjWords[rand()];
            sentence += SPACE + nounWords[rand()] + SPACE;
            sentence += (verbWords[rand()] + SPACE + prepositionWords[rand()]);
            sentence += (SPACE + artWords[rand()] + SPACE + nounWords[rand()]);
            sentence += PERIOD;
          /* Generate sentence for level hard */
        } else {
            sentence = artWords[rand()];
            char c = sentence.charAt(0);
            sentence = sentence.replace(c, Character.toUpperCase(c));
            sentence += SPACE + adjWords[rand()];
            sentence += SPACE + nounWords[rand()] + SPACE + "and";
            sentence += SPACE + artWords[rand()];
            sentence += SPACE + nounWords[rand()] + SPACE;
            sentence += (verbWords[rand()] + SPACE + prepositionWords[rand()]);
            sentence += (SPACE + artWords[rand()] + SPACE + nounWords[rand()]);
            sentence += PERIOD;
        }
        return sentence;
    }

    static int rand(){
        int randomInt = rand.nextInt() % NUMOFWORDS;
        if ( randomInt < 0 )
            randomInt += NUMOFWORDS;
        return randomInt;
    }
}
