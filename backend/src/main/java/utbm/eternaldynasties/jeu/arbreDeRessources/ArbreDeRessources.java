/**
 * Classe ArbreDeRessources : Elle regroupe toutes les ressources du jeu et leurs gestions.
 */

package utbm.eternaldynasties.jeu.arbreDeRessources;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe récupère, traites et conserve les données des ressources du jeu vidéo.
 */
public class ArbreDeRessources {
    private final Map<String,Map<String, Ressource>> listeRessources = new HashMap<>();
    private final Map<String, Map<Bonus, Ressource>> listeUpgrade = new HashMap<>();
    private final JSONObject jsonObject;

    /**
     * Récupère le fichier json en format objet et le traite pour extraire les données 'ressources'.
     */
    public ArbreDeRessources(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        for (Object type : jsonObject.keySet()) {
            String typeString = type.toString();
            Map<String, Ressource> map = new HashMap<>();
            JSONObject json = Json.objectToJsonObject((Map) jsonObject.get(typeString));
            for (Object nomRessource : json.keySet()) {
                map.put(nomRessource.toString(), new Ressource(nomRessource.toString(), (Map<String, Object>) json.get(nomRessource)));
            }
            this.listeRessources.put(typeString, map);
        }
        this.listeRessources.values().forEach(map->map.values().forEach(r -> r.getListeBonus().forEach((nomBonus, bonus) -> {
            if (this.listeUpgrade.containsKey(nomBonus)) {
                this.listeUpgrade.get(nomBonus).put(bonus, r);
            } else {
                Map<Bonus, Ressource> m = new HashMap<>();
                m.put(bonus, r);
                this.listeUpgrade.put(nomBonus, m);
            }
        })));
    }

    public void init(HashMap<String, Double> ressources) {
        for(String nom : ressources.keySet()){
            this.listeRessources.forEach((key, val)->{
                if(val.containsKey(nom)){
                    val.get(nom).setActive(true);
                }
            });
        }
    }

    public Map<String,Map<String, Ressource>> getListeRessources() {
        return listeRessources;
    }

    public Map<String, Ressource> getListeRessourcesAll() {
        Map<String, Ressource> map = new HashMap<>();
        listeRessources.forEach((key, val)->{
            map.putAll(val);
        });
        return map;
    }

    public void actualiseEstimationBonus(HashMap<String, Double> ressourcesActuelles){
        listeRessources.forEach((key, val)->val.forEach((key1,val1)->val1.getListeBonusEnString(ressourcesActuelles)));
    }

    public Ressource getRessource(String nomRessource) {
        final Ressource[] ressource = {null};
        this.listeRessources.values().forEach(map->{
            if(ressource[0] ==null){
                ressource[0] = map.getOrDefault(nomRessource, null);
            }
        });
        return ressource[0];
    }

    public Map<Bonus, Ressource> getListeRessourceEnFonctionBonus(String nomRessourceDemande, HashMap<String, Double> ressourcesActuelles){
        Map<Bonus, Ressource> val = new HashMap<>();
        this.listeUpgrade.get(nomRessourceDemande).forEach((bonus, ressource)->{
            if(!ressource.getNom().equals(nomRessourceDemande)){
                val.put(bonus.resume(ressourcesActuelles), ressource);
            }
        });
        return val;
    }

    /**
     * Converti l'arbre en string pour être transmis.
     */
    public String toString() {

        return Json.jsonToString(jsonObject);
    }
}