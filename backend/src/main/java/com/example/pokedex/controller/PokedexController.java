package com.example.pokedex.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pokedex.model.PokemonDetail;
import com.example.pokedex.service.PokemonService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:3000")
public class PokedexController {

    private final PokemonService service;

    public PokedexController(PokemonService service) {
        this.service = service;
    }

    @GetMapping("/{name}")
    public Mono<PokemonDetail> pokemon(@PathVariable String name) {
        return service.getDetails(name);
    }
}
