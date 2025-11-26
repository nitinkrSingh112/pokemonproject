package com.example.pokedex.model;

import java.util.List;
import java.util.Map;

public record PokemonDetail(
        Integer id,
        String name,
        String image,
        List<String> types,
        List<Map<String, Object>> stats
) {}
