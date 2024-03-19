package utbm.eternaldynasties.jeu.arbreRecherches;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArbreDeRecherches {
    private Map<String, Recherche> listeRecherches = new HashMap<>();
    private Map<String,Map<String, String>> arbrePourAffichage = new HashMap<>();
    private ArrayList<String> eres = new ArrayList<>();
    private Map<String, JSONObject> mapEre = new HashMap<>();

    public ArbreDeRecherches(JSONObject jsonObject) {
        for (Object key : jsonObject.keySet()) {
            String keyString = key.toString();
            if (this.listeRecherches.containsKey(keyString)) {
                this.listeRecherches.get(keyString).update((Map<String, Object>) jsonObject.get(key), this.listeRecherches);
            } else {
                this.listeRecherches.put(keyString, new Recherche(keyString, (Map<String, Object>) jsonObject.get(key), this.listeRecherches));
            }
        }
        this.listeEres();
        this.convertEnArbre();
    }

    public void init(ArrayList<String> recherches) {
        recherches.forEach(r -> this.listeRecherches.get(r).forceActive());
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

    public Map<String, String> recherchesEffectueesMap() {
        Map<String, String> map = new HashMap<>();
        for (Recherche recherche : this.listeRecherches.values().stream().filter(Recherche::getEtat).collect(Collectors.toList())) {
            map.put(recherche.getNom(), "");
        }
        return map;
    }

    public Recherche getRecherche(String nomRecherche) {
        return this.listeRecherches.get(nomRecherche);
    }

    public List<Recherche> recherchesPossibles() {
        return this.listeRecherches.values().stream()
                .filter(Recherche::checkConditions)
                .filter(r -> !r.getEtat())
                .filter(Recherche::getRecherchePossible)
                .filter(Recherche::actualiseEtat)
                .collect(Collectors.toList());
    }

    public void listeEres() {
        this.eres = (ArrayList<String>) this.listeRecherches.values().stream()
                .filter(r -> r.getId() == -1)
                .map(Recherche::getNom)
                .collect(Collectors.toList());
    }

    public void convertEnArbre() {

        for (Recherche recherche : this.listeRecherches.values()) {
            if (recherche.getId() != -1) {
                Map<String, String> map = new HashMap<>();
                map.put("key", "" + recherche.getId());
                map.put("text", recherche.getNom());
                map.put("fill", "#f68c06");
                map.put("stroke", "#4d90fe");

                Map<String, Recherche> dependances = recherche.getDependances();
                if (dependances != null && !dependances.isEmpty()) {
                    dependances.values().stream().filter(r -> !this.eres.contains(r.getNom())).findFirst().ifPresent(r -> map.put("parent", r.getId() + ""));
                }
                this.arbrePourAffichage.put(recherche.getNom(),map);
            }
        }
    }

    public Map<String, Recherche> getListeRecherches() {
        return listeRecherches;
    }

    public Map<String, Map<String, String>> getArbrePourAffichage() {
        return arbrePourAffichage;
    }

    public Map<String,ArrayList<String>> getEres() {
        Map<String,ArrayList<String>> map = new HashMap<>();
        this.eres.forEach(ere->map.put(this.listeRecherches.get(ere).getNom(), this.listeRecherches.get(ere).getRecherchesEre()));
        return map;
    }

    public Map<String, JSONObject> getEresObj() {
        return mapEre;
    }

    public void setEresObj(JSONObject eresObj) {

        for(Object object : (List)eresObj.get("Ere")){
            JSONObject jsonObject = Json.objectToJsonObject((Map) object);
            this.mapEre.put((String) jsonObject.get("nom"),jsonObject);
        }
    }

    public String toString() {
        Map<String, Object> liste = new HashMap<>();
        Map<String, ArrayList<String>> lesEres = this.getEres();
        this.listeRecherches.values().forEach(r -> {
            if (!lesEres.containsKey(r.getNom())) {
                liste.put(r.getNom(), r.getJsonObjet());
            }
        });

        lesEres.forEach((key, val)->{
            val.forEach(recherche->{
                this.listeRecherches.get(recherche).getJsonObjet().put("Ere",this.mapEre.get(key));
            });
        });
        return Json.jsonToString(liste);
    }
}
