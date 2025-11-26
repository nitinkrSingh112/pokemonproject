package com.example.pokedex.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EvolutionUtil {

    public static List<Map<String, Object>> flattenChain(Map<String, Object> chainRoot) {
        List<Map<String, Object>> out = new ArrayList<>();
        walk(chainRoot, out, null);
        return out;
    }

    @SuppressWarnings("unchecked")
    private static void walk(Map<String, Object> node, List<Map<String, Object>> out, String trigger) {
        if (node == null) return;

        Map<String, Object> species = (Map<String, Object>) node.get("species");
        if (species == null) return;

        String name = Optional.ofNullable(species.get("name")).map(Object::toString).orElse("unknown");

        // SAFE: do NOT use Map.of(), since it doesn't allow null values
        Map<String, Object> entry = new HashMap<>();
        entry.put("name", name);
        entry.put("trigger", trigger);

        out.add(entry);

        List<Map<String, Object>> evolvesTo =
                (List<Map<String, Object>>) node.get("evolves_to");

        if (evolvesTo == null || evolvesTo.isEmpty()) return;

        for (Map<String, Object> nxt : evolvesTo) {
            List<Map<String, Object>> details =
                    (List<Map<String, Object>>) nxt.get("evolution_details");

            String trg = null;
            if (details != null && !details.isEmpty()) {
                Map<String, Object> d = details.get(0);
                trg = Optional.ofNullable(d.get("trigger"))
                        .map(Object::toString)
                        .orElse(null);
            }

            walk(nxt, out, trg);
        }
    }
}
