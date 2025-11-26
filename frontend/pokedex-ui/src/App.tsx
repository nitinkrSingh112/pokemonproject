import { useState } from "react";
import "./App.css";

interface Pokemon {
  id?: number;
  name: string;
  image: string;
  types: string[];
  stats: { name: string; base: number }[];
}

function App() {
  const [pokemonName, setPokemonName] = useState("");
  const [pokemon, setPokemon] = useState<Pokemon | null>(null);
  const [isFlipped, setIsFlipped] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function searchPokemon() {
    const q = pokemonName.trim().toLowerCase();
    if (!q) return;
    setLoading(true);
    setError(null);
    setIsFlipped(false);

    try {
      const res = await fetch(`http://localhost:8080/api/pokemon/${encodeURIComponent(q)}`);
      if (!res.ok) {
        if (res.status === 404) setError("Pokémon not found");
        else setError("Network error");
        setPokemon(null);
        return;
      }
      const data = await res.json();
      setPokemon(data);
    } catch (e) {
      setError("Failed to fetch");
      setPokemon(null);
    } finally {
      setLoading(false);
    }
  }

  function onKey(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === "Enter") searchPokemon();
  }

  return (
    <div className="app-root">
      <div className="app-container">
        <h1 className="title">Pokedex</h1>

        <div className="search-row">
          <input
            value={pokemonName}
            onChange={(e) => setPokemonName(e.target.value)}
            onKeyDown={onKey}
            placeholder="Type a Pokémon name (e.g. pikachu) and press Enter"
            aria-label="Search pokemon"
            className="search-input"
          />
          <button className="search-btn" onClick={searchPokemon} disabled={loading}>
            {loading ? "Loading…" : "Search"}
          </button>
        </div>

        {error && <div className="error">{error}</div>}

        {/* Centered flip card area */}
        <div className="center-area">
          {!pokemon && !loading && <div className="hint">Search for a Pokémon to see details</div>}

          {pokemon && (
            <div
              className={`flip-container ${isFlipped ? "flipped" : ""}`}
              onClick={() => setIsFlipped((s) => !s)}
              role="button"
              tabIndex={0}
              onKeyDown={(e) => e.key === "Enter" && setIsFlipped((s) => !s)}
            >
              <div className="flipper">
                {/* FRONT */}
                <div className="front">
                  <h2 className="poke-name">{pokemon.name}</h2>
                  <img className="poke-img" src={pokemon.image} alt={pokemon.name} />
                  <div className="types">
                    {pokemon.types.map((t) => (
                      <span key={t} className={`type-badge type-${t.replace(/\s+/g, "-")}`}>
                        {t}
                      </span>
                    ))}
                  </div>
                  <div className="click-note">Click card to view stats →</div>
                </div>

                {/* BACK */}
                <div className="back">
                  <h2 className="poke-name">Stats</h2>
                  <ul className="stats-list">
                    {pokemon.stats.map((s) => (
                      <li key={s.name} className="stat-row">
                        <span className="stat-name">{s.name}</span>
                        <span className="stat-val">{s.base}</span>
                      </li>
                    ))}
                  </ul>
                  <div className="click-note">Click card to flip back ←</div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
