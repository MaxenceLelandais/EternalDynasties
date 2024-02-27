package utbm.eternaldynasties.jeu.arbreRecherches;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArbreDeRecherches implements Cloneable{
    private Map<String, Recherche> listeRecherches = new HashMap<>();

    public ArbreDeRecherches(JSONObject jsonObject) {
        for (Object key : jsonObject.keySet()) {
            String keyString = key.toString();
            if (this.listeRecherches.containsKey(keyString)) {
                this.listeRecherches.get(keyString).update((Map<String, Object>) jsonObject.get(key), this.listeRecherches);
            } else {
                this.listeRecherches.put(keyString, new Recherche(keyString, (Map<String, Object>) jsonObject.get(key), this.listeRecherches));
            }
        }
    }

    public void init(ArrayList<String> recherches){
        recherches.forEach(r->this.listeRecherches.get(r).forceActive());
    }

    public boolean activerRecherche(String nomRecherche) {
        if (this.listeRecherches.containsKey(nomRecherche)) {
            this.listeRecherches.get(nomRecherche).activer();
            return true;
        }
        return false;
    }

    public List<Recherche> recherchesEffectuees() {
        return this.listeRecherches.values().stream().filter(Recherche::getEtat).collect(Collectors.toList());
    }

    public List<Recherche> recherchesPossibles() {
        return this.listeRecherches.values().stream()
                .filter(r->!r.getEtat())
                .filter(Recherche::getRecherchePossible)
                .filter(Recherche::actualiseEtat)
                .collect(Collectors.toList());
    }

    public String toString() {
        Map<String, Object> liste = new HashMap<>();
        this.listeRecherches.values().forEach(r->liste.put(r.getNom(), r.getJsonObjet()));
        return Json.jsonToString(liste);
    }

    @Override
    public ArbreDeRecherches clone() {
        try {
            return (ArbreDeRecherches) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
