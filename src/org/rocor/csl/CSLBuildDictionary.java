package org.rocor.csl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.io.ByteStreams;

public class CSLBuildDictionary {
    public static boolean isRussianOnly(String str) {
        for (char c : str.toCharArray()) {
            if ((c < 'a' || c > 'я') && (c < '0' || c > '9') && c != '?' && c != '-')
                return false;
        }
        return true;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        InputStream in = System.in;
        if (args.length > 0) {
            in = new FileInputStream(new File(args[0]));
        }
        String text = new String(ByteStreams.toByteArray(in), "utf8");
        if (in != System.in)
            in.close();
        String[] words = text.split("[\\s,.:\\/]+");
        HashMap<String, Set<String>> mapping = new HashMap<String, Set<String>>();
        for (String word : words) {
            word = word.toLowerCase().replaceAll("^[\\(\\)\\[\\]!*]+", "").replaceAll("[\\(\\)\\[\\]!*]+$", "");
            String rus = CSLToRus.convertWord(word);
            if (!isRussianOnly(rus)) {
                System.out.println(rus + "(" + word + ")");
            } else {
                Set<String> set = mapping.get(rus);
                if (set == null) {
                    set = new HashSet<String>();
                    mapping.put(rus, set);
                }
                set.add(word);
//                System.out.println(rus + " < " + word);
            }
        }

        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File("/tmp/dictionary")));
        oo.writeObject(mapping);
        oo.close();
    }
}
