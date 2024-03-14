/**
 * Classe ArbreDeRessources : Elle regroupe toutes les ressources du jeu et leurs gestions.
 */

package utbm.eternaldynasties.jeu.arbreDeRessources;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.utils.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe récupère, traites et conserve les données des ressources du jeu vidéo.
 */
public class ArbreDeRessources {
    private final Map<String, Ressource> listeRessources = new HashMap<>();
    private final Map<String, Map<Bonus, Ressource>> listeUpgrade = new HashMap<>();

    /**
     * Récupère le fichier json en format objet et le traite pour extraire les données 'ressources'.
     */
    public ArbreDeRessources(JSONObject jsonObject) {
        for (Object key : jsonObject.keySet()) {
            String keyString = key.toString();
            this.listeRessources.put(keyString, new Ressource(keyString, (Map<String, Object>) jsonObject.get(key)));
        }
        this.listeRessources.values().forEach(r -> r.getListeBonus().forEach((nomBonus, bonus) -> {
            if (this.listeUpgrade.containsKey(nomBonus)) {
                this.listeUpgrade.get(nomBonus).put(bonus, r);
            } else {
                Map<Bonus, Ressource> map = new HashMap<>();
                map.put(bonus, r);
                this.listeUpgrade.put(nomBonus, map);
            }
        }));
    }

    public Map<String, Ressource> getListeRessources() {
        return listeRessources;
    }

    public Ressource getRessource(String nomRessource) {
        return this.listeRessources.getOrDefault(nomRessource, null);
    }

    public Map<Bonus, Ressource> getListeRessourceEnFonctionBonus(String nomRessourceDemande, HashMap<String, Long> ressourcesActuelles){
        Map<Bonus, Ressource> val = new HashMap<>();
        this.listeUpgrade.get(nomRessourceDemande).forEach((bonus, ressource)->{
            if(!ressource.getNom().equals(nomRessourceDemande)){
                val.put(bonus.resume(ressourcesActuelles), ressource);
            }
        });
        return val;
    }

    /**
     * Converti l'arbre en string pour être transmit.
     */
    public String toString() {
        Map<String, Object> liste = new HashMap<>();
        this.listeRessources.values().forEach(r -> liste.put(r.getNom(), r.getJsonObjet()));
        return Json.jsonToString(liste);
    }
}