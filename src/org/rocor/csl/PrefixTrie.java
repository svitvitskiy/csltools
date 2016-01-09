package org.rocor.csl;

import java.util.HashMap;
import java.util.Map;

public class PrefixTrie {
    private Node nodes = new Node();

    private static class Node {
        String value;
        Map<Integer, Node> children = new HashMap<Integer, Node>();
    }

    public static class Prefix {
        int length;
        String mapped;

        public Prefix(String found, int depth) {
            mapped = found;
            length = depth;
        }
    }

    public void put(String prefix, String mapped) {
        Node cur = nodes;
        for (char c : prefix.toCharArray()) {
            Node next = cur.children.get((int) c);
            if (next == null) {
                next = new Node();
                cur.children.put((int) c, next);
            }
            cur = next;
        }
        cur.value = mapped;
    }

    public Prefix find(String str) {
        Node cur = nodes;
        int depth = 1, foundDepth = 0;
        String found = null;
        for (char c : str.toCharArray()) {
            Node next = cur.children.get((int) c);
            if (next == null) {
                break;
            } else {
                cur = next;
                if (next.value != null) {
                    found = next.value;
                    foundDepth = depth;
                }
            }
            ++depth;
        }
        return found == null ? null : new Prefix(found, foundDepth);
    }
}
