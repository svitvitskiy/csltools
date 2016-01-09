package org.rocor.csl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.rocor.csl.PrefixTrie.Prefix;

import com.google.common.io.ByteStreams;

public class RusToCSL {
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ClassNotFoundException {
        ObjectInputStream oi = new ObjectInputStream(new FileInputStream(new File("/tmp/dictionary")));
        Map<String, Set<String>> mapping = (Map<String, Set<String>>) oi.readObject();
        oi.close();

        String text = new String(ByteStreams.toByteArray(System.in), "utf8");

        int aggregate = findDelim(text, 0);

        String[] words = text.split("[ \n\t,\\.:\\/]+");
        for (String word : words) {
            word = word.toLowerCase();
            aggregate += word.length();
            int delimLength = findDelim(text, aggregate);
            String delim = text.substring(aggregate, aggregate + delimLength);
            aggregate += delimLength;

            if (word.matches("^[0-9]+$")) {
                System.out.print(numToCsl(Integer.parseInt(word)) + delim);
            } else {
                Set<String> csl = mapping.get(word);
                if (csl == null || csl.size() == 0)
                    System.out.print(toCslByRule(word) + delim);
                else if (csl.size() == 1) {
                    System.out.print(csl.iterator().next() + delim);
                } else {
                    System.out.print("(");
                    for (String string : csl) {
                        System.out.print(string + "  ");
                    }
                    System.out.print(")" + delim);
                }
            }
        }
    }

    private static int findDelim(String text, int aggregate) {
        int delimLength = 0;
        for (int i = aggregate; i < text.length(); i++) {
            if (" \n\t,.:\\/".indexOf(text.charAt(i)) == -1) {
                delimLength = i - aggregate;
                break;
            }
        }
        return delimLength;
    }

    private static PrefixTrie rules = new PrefixTrie();

    static {
        rules.put("кс", "…");
        rules.put("пс", "p");
        rules.put("мати", "м™и");
        rules.put("церк", "цRк");
        rules.put("царст", "цrт");
        rules.put("свят", "с™");
        rules.put("благ", "бlг");
        rules.put("христ", "хrт");
        rules.put("рождест", "ржcт");
        rules.put("отец", "nц");
        rules.put("цар", "цR");
        rules.put("приподоб", "прпdб");
        rules.put("иисус", "ї}с");
        rules.put("крещ", "кRщ");
        rules.put("молитв", "мlтв");
        rules.put("милост", "млcт");
        rules.put("господ", "гD");
        rules.put("бог", "бG");
        rules.put("спас", "сп7с");
        rules.put("боже", "б9е");
        rules.put("Боже", "Б9е");
        rules.put("солнцу", "сlнцу");
        rules.put("пресвят", "прес™");
        rules.put("богородиц", "бцd");
        rules.put("чист", "чcт");
        rules.put("Богомат", "бGом™");
        rules.put("Богородиц", "бцd");
        rules.put("госпож", "гпcж");
        rules.put("свята", "с™a");
        rules.put("Свят", "С™");
        rules.put("отче", "џ§е");
        rules.put("престол", "пrт0л");
        rules.put("отц", "nц");
        rules.put("дух", "д¦");
        rules.put("матер", "м™р");
        rules.put("челов", "чlв");
        rules.put("крещ", "кRщ");
        rules.put("крест", "кrт");
        rules.put("спас", "сп7с");
        rules.put("крещ", "кRщ");
        rules.put("сын", "сн7");
        rules.put("дух", "д¦ъ");
        rules.put("предт", "п®т");
        rules.put("ангел", "ѓгGлъ");
        rules.put("владык", "вLк");
        rules.put("владыч", "вLч");
        rules.put("мари", "мRj");
        rules.put("евангел", "є3ђл");
        rules.put("апостол", "Ґпcл");
        rules.put("Апостол", "Ґпcл");
        rules.put("прор", "прbр");
        rules.put("Прор", "Прbр");
        rules.put("Пребодоб", "Прпdб");
        rules.put("милосерд", "млcрд");
        rules.put("праведн", "првdн");
        rules.put("честн", "чтcн");
        rules.put("имярек", "и4м>к");
        rules.put("ангел", "ѓгGл");
        rules.put("Мучени", "М§н");
        rules.put("мучени", "м§н");
        rules.put("учитель", "ўч™л");
        rules.put("господ", "гd");
        rules.put("иисус", "їи&с");
        rules.put("христос", "х&с");
        rules.put("имярек", "и$м>къ");
    }

    private static String toCslByRule(String word) {
        StringBuilder output = new StringBuilder();
        int i = 0;
        while (!word.isEmpty()) {
            Prefix prefix = rules.find(word);
            if (prefix != null) {
                output.append(prefix.mapped);
                word = word.substring(prefix.length);
                i += prefix.length;
            } else {
                char c = word.charAt(0);
                if (c == 'я') {
                    output.append(i == 0 ? 'я' : 'z');
                } else {
                    output.append(c);
                }
                word = word.substring(1);
                i++;
            }
        }
        return output.toString();
    }

    private static String[] numbers = new String[] { "", "№", "в7", "G", "д7", "є7", "ѕ7", "з7", "}", "f7", "‹", };
    private static String[] numbers1 = new String[] { "", "", "к", "л", "м", "н", "…", "о", "п", "ч", };
    private static String[] numbers2 = new String[] { "", "а", "в", "г", "д", "є", "ѕ", "з", "и", "f", };
    private static String[] numbers3 = new String[] { "", "р7", "с7", "™", "у7", "ф7", "х7", "p7", "w7", "ц7" };

    private static String numToCsl(int number) {
        if (number <= 10) {
            return numbers[number];
        } else if (number < 20) {
            return numbers[number - 10] + "i";
        } else if (number < 100) {
            return numbers1[number / 10] + "7" + numbers2[number % 10];
        } else if (number < 1000) {
            return numbers3[number / 100] + numbers1[(number % 100) / 10] + numbers2[number % 10];
        }
        return "";
    }

    private static String capitalize(String string, boolean isCapitalized) {
        return !isCapitalized ? string : Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}