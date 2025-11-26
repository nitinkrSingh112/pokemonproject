package com.example.pokedex.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.pokedex.model.PokemonDetail;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class PokemonService {

    private final WebClient client;

    public PokemonService(WebClient pokeWebClient) {
        this.client = pokeWebClient;
    }

    public Mono<PokemonDetail> getDetails(String rawName) {
        String name = rawName.trim().toLowerCase();

        return client.get()
                .uri("/pokemon/{name}", name)
                .retrieve()
                .bodyToMono(Map.class)
                .map(data -> {

                    Map<String, Object> sprites = (Map<String, Object>) data.get("sprites");
                    Map<String, Object> other = sprites != null ? (Map<String, Object>) sprites.get("other") : null;
                    Map<String, Object> artwork = other != null ? (Map<String, Object>) other.get("official-artwork") : null;
                    String image = artwork != null ? (String) artwork.get("front_default") : null;

                    List<Map<String, Object>> typesList = (List<Map<String, Object>>) data.get("types");
                    List<String> typeNames = typesList.stream()
                            .map(t -> (Map<String, Object>) t.get("type"))
                            .map(t -> (String) t.get("name"))
                            .toList();

                    List<Map<String, Object>> statsList = (List<Map<String, Object>>) data.get("stats");
                    List<Map<String, Object>> simplifiedStats = statsList.stream()
                            .map(s -> Map.of(
                                    "name", ((Map<String, Object>) s.get("stat")).get("name"),
                                    "base", s.get("base_stat")
                            ))
                            .toList();

                    return new PokemonDetail(
                            (Integer) data.get("id"),
                            (String) data.get("name"),
                            image,
                            typeNames,
                            simplifiedStats
                    );
                });
    }
}
