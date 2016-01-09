package org.rocor.csl;

import java.io.IOException;

import org.rocor.csl.PrefixTrie.Prefix;

import com.google.common.io.ByteStreams;

public class CSLToRus {
    private static PrefixTrie rules = new PrefixTrie();

    static {
        rules.put("б9", "бож");
        rules.put("ћ", "я");
        rules.put("ћ", "я");
        rules.put("S", "я");
        rules.put("Ћ", "я");
        rules.put("±", "я");
        rules.put("k", "я");
        rules.put("љ", "я");
        rules.put("ё", "е");
        rules.put("є", "е");
        rules.put("Ё", "е");
        rules.put("э", "е");
        rules.put("E", "е");
        rules.put("B", "е");
        rules.put("Ё", "е");
        rules.put("a", "а");

        rules.put("ў", "у");
        rules.put("ќ", "у");
        rules.put("Y", "у");
        rules.put("u", "у");

        rules.put("z", "я");
        rules.put("s", "я");
        rules.put("Їа", "Я");
        rules.put("іа", "я");
        rules.put("‰", "я");

        rules.put("h", "ы");

        rules.put("A", "а");
        rules.put("ѓ", "а");
        rules.put("ґ", "а");
        rules.put("†", "а");
        rules.put("Ѓ", "А");

        rules.put("ъ", "");
        rules.put("1", "");
        rules.put("2", "");
        rules.put("3", "");
        rules.put("4", "");
        rules.put("5", "");
        rules.put("6", "");
        rules.put("7", "");
        rules.put("8", "ъ");
        rules.put("3", "");
        rules.put("#", "");
        rules.put("$", "");
        rules.put("%", "");

        rules.put("њ", "о");
        rules.put("w", "о");
        rules.put("0", "о");
        rules.put("џ", "о");
        rules.put("H", "о");
        rules.put("n", "о");

        rules.put("t", "от");
        rules.put("я", "от");
        rules.put("T", "От");

        rules.put("j", "и");
        rules.put("ї", "и");
        rules.put("і", "и");
        rules.put("ј", "и");
        rules.put("›", "и");
        rules.put("Ђ", "и");
        rules.put("f", "ф");
        rules.put("v", "в");
        rules.put("ѕ", "з");

        rules.put("№", "1");
        rules.put("в7", "2");
        rules.put("G", "3");
        rules.put("д7", "4");
        rules.put("є7", "5");
        rules.put("ѕ7", "6");
        rules.put("з7", "7");
        rules.put("}", "8");
        rules.put("f7", "9");
        rules.put("№i", "11");
        rules.put("в7i", "12");
        rules.put("Gi", "13");
        rules.put("д7i", "14");
        rules.put("є7i", "15");
        rules.put("ѕ7i", "16");
        rules.put("з7i", "17");
        rules.put("}i", "18");
        rules.put("f7i", "19");
        rules.put("к7", "20");
        rules.put("к7д", "24");
        rules.put("к7є", "25");
        rules.put("к7ѕ", "26");
        rules.put("™ѕ", "306");
        rules.put("™и", "308");
        rules.put("ст&", "свят");
        rules.put("^", "");
        rules.put("~", "");
        rules.put("@", "");
        rules.put("бг&", "бог");
        rules.put("поднб\\c", "поднебес");
        rules.put("™л", "320");
        rules.put("™", "300");
        rules.put("™а", "301");
        rules.put("™i", "310");
        rules.put("™f", "309");
        rules.put("™з", "307");
        rules.put("™в", "302");
        rules.put("™а", "301");

        rules.put("™к", "320");
        rules.put("м™", "мат");
        rules.put("™є", "305");
        rules.put("™г", "303");
        rules.put("™д", "304");

        rules.put("ст&о~", "свято");

        rules.put("м7", "40");
        rules.put("н7", "50");
        rules.put("…", "кс");
        rules.put("p", "пс");
        rules.put("<", "");

        rules.put(";", "?");
        rules.put("‹", "10");
        rules.put("‡", "и");
        rules.put("ђ", "и");

        rules.put("м™и", "мати");
        rules.put("цRк", "церк");
        rules.put("цrт", "царст");
        rules.put("с™", "свят");
        rules.put("бlг", "благ");
        rules.put("хrт", "христ");
        rules.put("ржcт", "рождест");
        rules.put("nц", "отец");
        rules.put("цR", "цар");
        rules.put("прпdб", "приподоб");
        rules.put("ї}с", "иисус");
        rules.put("кRщ", "крещ");
        rules.put("мlтв", "молитв");
        rules.put("млcт", "милост");
        rules.put("гD", "господ");
        rules.put("бG", "бог");
        rules.put("сп7с", "спас");
        rules.put("б9е", "боже");
        rules.put("Б9е", "Боже");
        rules.put("сlнцу", "солнцу");
        rules.put("прес™", "пресвят");
        rules.put("бцd", "богородиц");
        rules.put("чcт", "чист");
        rules.put("бGом™", "Богомат");
        rules.put("бцd", "Богородиц");
        rules.put("гпcж", "госпож");
        rules.put("с™a", "свята");
        rules.put("С™", "Свят");
        rules.put("џ§е", "отче");
        rules.put("пrт0л", "престол");
        rules.put("nц", "отц");
        rules.put("д¦", "дух");
        rules.put("м™р", "матер");
        rules.put("чlв", "челов");
        rules.put("кRщ", "крещ");
        rules.put("кrт", "крест");
        rules.put("сп7с", "спас");
        rules.put("кRщ", "крещ");
        rules.put("сн7", "сын");
        rules.put("д¦ъ", "дух");
        rules.put("п®т", "предт");
        rules.put("ѓгGлъ", "ангел");
        rules.put("вLк", "владык");
        rules.put("вLч", "владыч");
        rules.put("мRj", "мари");
        rules.put("є3ђл", "евангел");
        rules.put("Ґпcл", "апостол");
        rules.put("Ґпcл", "Апостол");
        rules.put("прbр", "прор");
        rules.put("Прbр", "Прор");
        rules.put("Прпdб", "Пребодоб");
        rules.put("млcрд", "милосерд");
        rules.put("првdн", "праведн");
        rules.put("чтcн", "честн");
        rules.put("и4м>к", "имярек");
        rules.put("ѓгGл", "ангел");
        rules.put("М§н", "Мучени");
        rules.put("м§н", "мучени");
        rules.put("ўч™л", "учитель");
        rules.put("гd", "господ");
        rules.put("їи&с", "иисус");
        rules.put("х&с", "христос");
        rules.put("и$м>къ", "имярек");
    }

    public static String convertCSLToRus(String csl) {
        String[] words = csl.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            word = convertWord(result, word);
            result.append(" ");
        }
        return result.toString();
    }

    public static String convertWord(String word) {
        StringBuilder result = new StringBuilder();
        convertWord(result, word);
        return result.toString();
    }

    private static String convertWord(StringBuilder result, String word) {
        while (!word.isEmpty()) {
            Prefix prefix = rules.find(word);
            if (prefix == null) {
                result.append(word.charAt(0));
                word = word.substring(1);
            } else {
                result.append(prefix.mapped);
                word = word.substring(prefix.length);
            }
        }
        return word;
    }

    public static void main(String[] args) throws IOException {
        String text = new String(ByteStreams.toByteArray(System.in), "utf8");
        System.out.println(convertCSLToRus(text));
    }
}
