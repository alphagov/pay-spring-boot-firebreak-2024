package uk.gov.pay.spring.utils;

import java.util.HashSet;
import java.util.Set;

public class RandomIdentifierGenerator {

    private static final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    private static final java.util.Random rand = new java.util.Random();

    private static final Set<String> identifiers = new HashSet<>();

    public static String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().isEmpty()) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
}
