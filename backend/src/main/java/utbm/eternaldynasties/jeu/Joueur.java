/**
 * Classe Joueur : Traite toutes les actions possibles et les données d'un joueur.
 */

package utbm.eternaldynasties.jeu;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.ArbreDeRessources;
import utbm.eternaldynasties.jeu.arbreDeRessources.Bonus;
import utbm.eternaldynasties.jeu.arbreDeRessources.Ressource;
import utbm.eternaldynasties.jeu.arbreDeRessources.RessourceSimplifee;
import utbm.eternaldynasties.jeu.arbreEnvironnements.Environnement;
import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.utils.Json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Joueur {

    private String civilisation;
    private Environnement environnement;
    private String debutDeLaPartie;
    private final HashMap<String, Double> ressources = new HashMap<>();
    private final ArrayList<String> recherches = new ArrayList<>();
    private final ArbreDeRecherches arbreDeRecherche;
    private final ArbreDeRessources arbreDeRessources;

    private HashMap<String, RessourceSimplifee> mapRessourcesSimplifiees;

    /**
     * Traduit les données.
     * @param jsonObject
     * @param arbreDeRecherche
     * @param arbreDeRessources
     * @param environnement
     */
    public Joueur(JSONObject jsonObject, ArbreDeRecherches arbreDeRecherche, ArbreDeRessources arbreDeRessources, Environnement environnement) {
        this.arbreDeRecherche = arbreDeRecherche;
        this.arbreDeRessources = arbreDeRessources;
        if (((Map) jsonObject).containsKey("Civilisation")) {
            this.environnement = environnement;
            this.civilisation = ((Map) jsonObject).get("Civilisation").toString();
            this.debutDeLaPartie = (String) ((Map) jsonObject).get("Début de la partie");
            HashMap<String, String> mapRessource = (HashMap<String, String>) ((Map) jsonObject).get("Ressources");
            HashMap<String, String> caracteristiquesCollectes = (HashMap<String, String>) ((Map) jsonObject).get("Caracteristiques collectes");
            for (String key : mapRessource.keySet()) {
                this.ressources.put(key, Double.parseDouble(mapRessource.get(key)));
                if (caracteristiquesCollectes.containsKey(key)) {
                    this.arbreDeRessources.getRessource(key).getListeBonus().replace(key, new Bonus(key, caracteristiquesCollectes.get(key)));
                }
            }
            HashMap<String, String> mapRecherches = (HashMap<String, String>) ((Map) jsonObject).get("Recherches");
            if (mapRecherches != null && !mapRecherches.isEmpty()) {
                this.recherches.addAll(mapRecherches.keySet());
            }
            this.arbreDeRecherche.init(this.recherches);
            this.arbreDeRessources.init(this.ressources);
            getRessourcesSimplifie();


        }
    }

    public void init(String civilisation, Environnement environnement) {

        this.civilisation = civilisation;
        this.environnement = environnement;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.debutDeLaPartie = dtf.format(LocalDateTime.now());
    }

    public List<Recherche> recherchesPossibles() {
        return this.arbreDeRecherche.recherchesPossibles();
    }

    public String activerRecherche(String nomRecherche) {
        Map<String, Double> listeCout = this.arbreDeRecherche.getRecherche(nomRecherche).getListeCout();
        StringBuilder txt = new StringBuilder(nomRecherche + " : RESSOURCES INSUFISANTES { ");
        boolean pbRessource = false;
        for (String nomRessource : listeCout.keySet()) {
            double val = this.ressources.get(nomRessource) - listeCout.get(nomRessource);
            if (val < 0) {
                txt.append(nomRessource).append(" : ").append(val).append("; ");
                pbRessource = true;
            }
        }
        if (pbRessource) {
            return txt + " }";
        }
        if (this.arbreDeRecherche.activerRecherche(nomRecherche)) {
            for (String nomRessource : listeCout.keySet()) {
                this.ressources.replace(nomRessource, this.ressources.get(nomRessource) - listeCout.get(nomRessource));
            }
            Map<String, Bonus> map = this.arbreDeRecherche.getRecherche(nomRecherche).getListeBonus();
            for (String key : map.keySet()) {
                Bonus bonusRessource = map.get(key);
                String nom = bonusRessource.getRessourceGenere();
                Bonus bonusInfos = this.arbreDeRessources.getRessource(nom).getListeBonus().get(nom);
                bonusInfos.add(bonusRessource);
                for(Map<String, Ressource> val : this.arbreDeRessources.getListeRessources().values()){
                    if(val.containsKey(key)){
                        val.get(key).setActive(true);
                    }
                }
                if (key.contains("Max-")) {
                    this.ressources.putIfAbsent(key, bonusInfos.estimationValeur(this.ressources));
                }
                this.ressources.putIfAbsent(key, 0.0);
            }
            save();
            return nomRecherche + ": RECHERCHE EFFECTUEE";
        }
        return nomRecherche + " : RECHERCHE NON DEBLOQUEE";
    }

    private void checkEtAddVal(Map<String, Bonus> bonus) {
        bonus.forEach((key, value) -> {
            double valeur = value.estimationValeur(this.ressources);
            if (this.ressources.containsKey("Max-" + key)) {
                if (this.ressources.get(key) + valeur <= this.ressources.get("Max-" + key)) {
                    this.ressources.replace(key, this.ressources.get(key) + valeur);
                }
            } else {
                this.ressources.replace(key, this.ressources.get(key) + valeur);
            }
        });
    }

    public HashMap<String, RessourceSimplifee> clickAchat(String nomRessource) {

        Map<String, Double> cout = this.arbreDeRessources.getRessource(nomRessource).getListeCout();
        Map<String, Bonus> bonus = this.arbreDeRessources.getRessource(nomRessource).getListeBonus();
        if ((cout != null && !cout.isEmpty())) {
            boolean pasAssezDeRessources = false;
            for (String key : cout.keySet()) {
                double diff = this.ressources.get(key) - cout.get(key);
                if (diff >= 0) {
                    this.ressources.replace(key, diff);
                } else {
                    pasAssezDeRessources = true;
                    break;
                }
            }
            if (!pasAssezDeRessources) {
                this.checkEtAddVal(bonus);
            }
        } else {
            this.checkEtAddVal(bonus);
        }

        save();
        HashMap<String, RessourceSimplifee> map = new HashMap<>();

        getRessources().forEach((key, value)->{
            if(!key.contains("Max-")) {
                RessourceSimplifee ressourceSimplifiee = this.mapRessourcesSimplifiees.get(key);
                ressourceSimplifiee.quantite = value;
                map.put(key, ressourceSimplifiee);
            }
        });

        return map;
    }


    public Map<Object, Object> tickBonus() {
        for (String ressource : this.ressources.keySet()) {
            Map<String, Double> cout = this.arbreDeRessources.getRessource(ressource).getListeCout();
            if (!(cout != null && !cout.isEmpty())) {
                tick(ressource);
            }
        }
        save();
        return toMap();
    }

    public void tick(String ressource) {
        Map<String, Bonus> bonus = this.arbreDeRessources.getRessource(ressource).getListeBonus();
        if (bonus != null && !bonus.isEmpty()) {
            for (String keyBonus : bonus.keySet()) {
                Bonus ressourceBonus = bonus.get(keyBonus);
                double nouvelleValeur = 0.0;

                double quantiteParSecondes = ressourceBonus.getQuantiteParSecondes();
                double pourcentageParSecondes = (ressourceBonus.getPourcentageParSecondes() / 100);

                nouvelleValeur += quantiteParSecondes + ((pourcentageParSecondes / 100) * nouvelleValeur);

                Map<String, Double> pourcentageParRessources = ressourceBonus.getPourcentageParRessources();
                for (String key : pourcentageParRessources.keySet()) {
                    if (this.ressources.containsKey(key)) {
                        nouvelleValeur += this.ressources.get(key) * (pourcentageParRessources.get(key) / 100);
                    }
                }
                Map<String, Double> quantiteParRessources = ressourceBonus.getQuantiteParRessources();
                for (String key : quantiteParRessources.keySet()) {
                    if (this.ressources.containsKey(key)) {
                        nouvelleValeur += this.ressources.get(key) * quantiteParRessources.get(key);
                    }
                }

                double val = this.ressources.get(ressource) + nouvelleValeur;
                if (this.ressources.containsKey("Max-" + ressource)) {
                    if (val >= this.ressources.get("Max-" + ressource)) {
                        val = this.ressources.get("Max-" + ressource);
                    }
                }
                this.ressources.replace(keyBonus, val);
            }
        }
    }

    private boolean decomposition(Map<String, Double> ressourcesComplexe, ArrayList<String> listeActions) {
        if (ressourcesComplexe.isEmpty()) {
            return true;
        } else {
            for (String nom : ressourcesComplexe.keySet()) {
                listeActions.add(nom + ":" + 1);
                Map<String, Double> besoin = this.arbreDeRessources.getRessource(nom).getListeCout();
                for (String nomBesoin : besoin.keySet()) {
                    if (this.arbreDeRessources.getRessource(nomBesoin).getListeCout().isEmpty()) {
                        listeActions.add(nomBesoin + ":" + besoin.get(nomBesoin));
                    } else {
                        if (ressourcesComplexe.containsKey(nomBesoin)) {
                            ressourcesComplexe.replace(nomBesoin, (ressourcesComplexe.get(nomBesoin) + besoin.get(nomBesoin)));
                        } else {
                            ressourcesComplexe.put(nomBesoin, besoin.get(nomBesoin));
                        }
                    }
                }
                double valeur = ressourcesComplexe.get(nom) - 1;
                if (valeur <= 0) {
                    ressourcesComplexe.remove(nom);
                } else {
                    ressourcesComplexe.replace(nom, valeur);
                }
                if (ressourcesComplexe.isEmpty()) {
                    return true;
                }
                if (this.decomposition(ressourcesComplexe, listeActions)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<String> besoinRessources(Map<String, Double> ressourcesDemandees) {

        Map<String, Double> ressourcesComplexe = new HashMap<>();

        ArrayList<String> listeActions = new ArrayList<>();
        ressourcesDemandees.forEach((key, value) -> {
            if (this.arbreDeRessources.getRessource(key).getListeCout().isEmpty()) {
                listeActions.add(key + ":" + value);
            } else {
                ressourcesComplexe.put(key, value);
            }
        });
        this.decomposition(ressourcesComplexe, listeActions);
        Collections.reverse(listeActions);
        return listeActions;
    }

    public void ajoutStockage(ArrayList<String> listeActions) {
        Map<String, Double> ressourcesMax = new HashMap<>();
        int position = 0;
        while (position < listeActions.size()) {
            String[] data = listeActions.get(position).split(":");
            String nomRessource = data[0];
            double quantite = Double.parseDouble(data[1]);
            Map<Bonus, Ressource> map;
            for (Bonus bonus : this.arbreDeRessources.getRessource(nomRessource).getListeBonus().values()) {
                if (bonus.getRessourceGenere().contains("Max-")) {
                    if (ressourcesMax.containsKey(bonus.getRessourceGenere())) {
                        ressourcesMax.replace(bonus.getRessourceGenere(), ressourcesMax.get(bonus.getRessourceGenere()) + bonus.estimationValeur(this.ressources));
                    } else {
                        ressourcesMax.put(bonus.getRessourceGenere(), bonus.estimationValeur(this.ressources));
                    }
                }
            }
            if (this.ressources.containsKey("Max-" + nomRessource)) {
                ressourcesMax.putIfAbsent("Max-" + nomRessource, this.ressources.get("Max-" + nomRessource));
                if (quantite > ressourcesMax.get("Max-" + nomRessource)) {
                    map = this.arbreDeRessources.getListeRessourceEnFonctionBonus("Max-" + nomRessource, this.ressources);
                    double valeur = 0;
                    Ressource ressourceAPrendre = null;
                    for (Bonus bonus : map.keySet()) {
                        double tmp = bonus.estimationValeur(this.ressources);
                        if (valeur == 0) {
                            valeur = tmp;
                            ressourceAPrendre = map.get(bonus);
                        } else {
                            if (valeur < tmp) {
                                valeur = tmp;
                                ressourceAPrendre = map.get(bonus);
                            }
                        }
                    }
                    if (ressourceAPrendre != null) {
                        double nbrRessources =((quantite - ressourcesMax.get("Max-" + nomRessource)) / (double) valeur + 0.5);
                        HashMap<String, Double> demande = new HashMap<>();
                        demande.put(ressourceAPrendre.getNom(), nbrRessources);
                        ArrayList<String> aRajouter = this.besoinRessources(demande);
                        listeActions.addAll(position, aRajouter);
                        position--;
                    }
                }
            }
            position++;
        }
    }

    public Environnement getEnvironnement() {
        return environnement;
    }

    public String getCivilisation() {
        return civilisation;
    }

    public String getDebutDeLaPartie() {
        return debutDeLaPartie;
    }

    public HashMap<String, Double> getRessources() {
        return ressources;
    }

    public HashMap<String, RessourceSimplifee> getRessourcesSimplifie() {

        this.mapRessourcesSimplifiees = new HashMap<>();
        ressources.forEach((nom, valeur) -> {
            if (!nom.contains("Max-")) {
                Ressource ressource = this.arbreDeRessources.getRessource(nom);
                this.mapRessourcesSimplifiees.put(
                        nom,
                        new RessourceSimplifee(nom, valeur, ressources.getOrDefault("Max-" + nom, -1.0), ressource.getType(), ressource.getId(),ressource.getValeurEchange()));
            }
        });
        return this.mapRessourcesSimplifiees;
    }

    public String getEreActuelle() {
        String nomEre = "";
        int id = 0;
        for (Recherche ere : this.arbreDeRecherche.recherchesEffectuees().stream()
                .filter(r -> (r.getId() <= -1 && r.getEtat())).toList()) {
            if (id > ere.getId()) {
                id = ere.getId();
                nomEre = ere.getNom();
            }
        }
        return nomEre;
    }


    public Map<String, Map<String, Ressource>> getArbreRessources() {

        this.arbreDeRessources.actualiseEstimationBonus(this.ressources);
        Map<String, Map<String, Ressource>> toutesLesRessources = this.arbreDeRessources.getListeRessources();
        Map<String, Map<String, Ressource>> nouvelArbre = new HashMap<>();
        for(String type : toutesLesRessources.keySet()){
            HashMap<String, Ressource> map = new HashMap<>();
            nouvelArbre.put(type, map);
            for(String nom : toutesLesRessources.get(type).keySet()){
                Ressource ressource = toutesLesRessources.get(type).get(nom);
                if(ressource.isActive()){
                    map.put(nom, ressource);
                }
            }
        }
        return nouvelArbre;
    }

    public ArrayList<String> getRecherches() {
        return recherches;
    }

    public ArbreDeRecherches getArbreDeRecherche() {
        return arbreDeRecherche;
    }

    public ArbreDeRessources getArbreDeRessources() {
        return arbreDeRessources;
    }

    public Map<Object, Object> toMap() {
        Map<Object, Object> data = new HashMap<>();
        data.put("Civilisation", this.civilisation);
        data.put("Environnement", this.environnement.getNom());
        data.put("Début de la partie", this.debutDeLaPartie);
        Map<String, String> map = new HashMap<>();
        for (String key : this.ressources.keySet()) {
            map.put(key, String.valueOf(this.ressources.get(key)));
        }
        HashMap<String, String> caracteristiquesCollectes = new HashMap<>();
        data.put("Ressources", map);
        for (String key : this.ressources.keySet()) {
            if (!key.contains("Max-")) {
                caracteristiquesCollectes.put(key, this.arbreDeRessources.getRessource(key).getListeBonus().get(key).toString());
            }
        }
        data.put("Caracteristiques collectes", caracteristiquesCollectes);
        data.put("Recherches", this.arbreDeRecherche.recherchesEffectueesMap());
        return data;
    }

    public void save() {
        Json.save("src/main/resources/sauvegardes/" + this.civilisation + "-" + this.environnement.getNom() + ".save", toMap());
    }
}
