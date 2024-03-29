package utbm.eternaldynasties.jeu;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.ArbreDeRessources;
import utbm.eternaldynasties.jeu.arbreDeRessources.Bonus;
import utbm.eternaldynasties.jeu.arbreDeRessources.Ressource;
import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Joueur {

    private String nom;
    private String civilisation;
    private long tempsDeJeu;
    private final long tempsDemarrage;
    private long debutDeLaPartie;
    private HashMap<String, Long> ressources = new HashMap<>();
    private ArrayList<String> recherches = new ArrayList<>();
    private final ArbreDeRecherches arbreDeRecherche;
    private final ArbreDeRessources arbreDeRessources;

    public Joueur(JSONObject jsonObject, ArbreDeRecherches arbreDeRecherche, ArbreDeRessources arbreDeRessources) {
        this.arbreDeRecherche = arbreDeRecherche;
        this.arbreDeRessources = arbreDeRessources;
        Map data = jsonObject;
        if (data.containsKey("Nom")) {
            this.nom = data.get("Nom").toString();
            this.civilisation = data.get("Civilisation").toString();
            this.tempsDeJeu = Long.parseLong((String) data.get("Temps de jeu"));
            this.debutDeLaPartie = Long.parseLong((String) data.get("Début de la partie"));
            HashMap<String, String> mapRessource = (HashMap<String, String>) data.get("Ressources");
            HashMap<String, String> caracteristiquesCollectes = (HashMap<String, String>) data.get("Caracteristiques collectes");
            for (String key : mapRessource.keySet()) {
                this.ressources.put(key, Long.parseLong(mapRessource.get(key)));
                if (caracteristiquesCollectes.containsKey(key)) {
                    this.arbreDeRessources.getRessource(key).getListeBonus().replace(key, new Bonus(key, caracteristiquesCollectes.get(key)));
                }
            }
            HashMap<String, String> mapRecherches = (HashMap<String, String>) data.get("Recherches");
            if (mapRecherches != null && !mapRecherches.isEmpty()) {
                this.recherches.addAll(mapRecherches.keySet());
            }
            this.arbreDeRecherche.init(this.recherches);
        }
        this.tempsDemarrage = System.currentTimeMillis();
    }

    public void init(String informations) {
        if (informations.contains(":")) {
            this.nom = informations.split(":")[0];
            this.civilisation = informations.split(":")[1];
        } else {
            this.nom = informations;
        }
        this.tempsDeJeu = 0L;
        this.debutDeLaPartie = System.currentTimeMillis();
    }

    public Map<Object, Object> toMap() {
        Map<Object, Object> data = new HashMap<>();
        data.put("Nom", this.nom);
        data.put("Civilisation", this.civilisation);
        data.put("Temps de jeu", this.tempsDeJeu + "");
        data.put("Début de la partie", this.debutDeLaPartie + "");
        Map<String, String> map = new HashMap<>();
        for (String key : this.ressources.keySet()) {
            map.put(key, String.valueOf(this.ressources.get(key)));
        }
        HashMap<String, String> caracteristiquesCollectes = new HashMap<>();
        data.put("Ressources", map);
        for (String key : this.ressources.keySet()) {
            caracteristiquesCollectes.put(key, this.arbreDeRessources.getRessource(key).getListeBonus().get(key).toString());
        }
        data.put("Caracteristiques collectes", caracteristiquesCollectes);
        data.put("Recherches", this.arbreDeRecherche.recherchesEffectueesMap());
        return data;
    }

    public void save() {
        this.tempsDeJeu += System.currentTimeMillis() - this.tempsDemarrage;
        Json.save("src/main/resources/sauvegardes/" + this.nom + ".save", toMap());
    }

    public List<Recherche> recherchesPossibles() {
        return this.arbreDeRecherche.recherchesPossibles();
    }

    public boolean activerRecherche(String nomRecherche) {
        if (this.arbreDeRecherche.activerRecherche(nomRecherche)) {
            Map<String, Bonus> map = this.arbreDeRecherche.getRecherche(nomRecherche).getListeBonus();
            for (String key : map.keySet()) {
                for (Bonus bonusRessource : this.arbreDeRessources.getRessource(key).getListeBonus().values()) {
                    String nom = bonusRessource.getRessourceGenere();
                    this.arbreDeRessources.getRessource(nom).getListeBonus().get(nom).add(bonusRessource);
                }
                this.ressources.putIfAbsent(key, 0L);
            }
            save();
            return true;
        }
        return false;
    }

    public Map<Object, Object> clickAchat(String ressource) {
        Map<String, Long> cout = this.arbreDeRessources.getRessource(ressource).getListeCout();
        if ((cout != null && !cout.isEmpty())) {
            for (String keyCout : cout.keySet()) {
                long val = this.ressources.get(keyCout) - cout.get(keyCout);
                if (val >= 0) {
                    this.ressources.replace(keyCout, val);
                    Bonus bRessource = this.arbreDeRessources.getRessource(ressource).getListeBonus().get(ressource);
                    this.ressources.replace(ressource, this.ressources.get(ressource)+(long) (bRessource.getQuantite() * (1 + bRessource.getPourcentage()/100)));
                    for (Bonus bonusRessource : this.arbreDeRessources.getRessource(ressource).getListeBonus().values()) {
                        String nom = bonusRessource.getRessourceGenere();
                        if (!nom.equals(ressource)) {
                            if(nom.contains("Max-")){
                                String nomRessourceMax = nom.replace("Max-","");
                                if (!nomRessourceMax.equals(ressource)) {
                                    Map<String, Bonus> liste = this.arbreDeRessources.getRessource(nomRessourceMax).getListeBonus();
                                    liste.putIfAbsent(nom, new Bonus(nom, "0"));
                                    liste.get(nom).addQuantite(bonusRessource.getQuantite());
                                }
                            }
                            this.arbreDeRessources.getRessource(nom).getListeBonus().get(nom).add(bonusRessource);
                        }
                    }
                }
            }
        } else {
            Map<String, Bonus> bonus = this.arbreDeRessources.getRessource(ressource).getListeBonus();
            if (bonus != null && !bonus.isEmpty()) {
                for (String keyBonus : bonus.keySet()) {
                    long val = this.ressources.get(ressource);
                    if(!keyBonus.equals("Max-"+ressource)){
                        val += (long) (bonus.get(keyBonus).getQuantite() * (1 + bonus.get(keyBonus).getPourcentage() / 100));
                        if(keyBonus.equals(ressource)){
                            if(val>=bonus.get("Max-"+ressource).getQuantite()){
                                val = (long)bonus.get("Max-"+ressource).getQuantite();
                            }
                        }
                        this.ressources.replace(keyBonus, val);
                    }
                }
            }
        }

        save();
        return toMap();
    }


    public Map<Object, Object> tickBonus() {
        for (String ressource : this.ressources.keySet()) {
            Map<String, Long> cout = this.arbreDeRessources.getRessource(ressource).getListeCout();
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
                Double nouvelleValeur = 0.0;

                Double quantiteParSecondes = ressourceBonus.getQuantiteParSecondes();
                Double pourcentageParSecondes = ressourceBonus.getPourcentageParSecondes();

                nouvelleValeur += quantiteParSecondes + (pourcentageParSecondes * nouvelleValeur);

                Map<String, Double> pourcentageParRessources = ressourceBonus.getPourcentageParRessources();
                for (String key : pourcentageParRessources.keySet()) {
                    if (this.ressources.containsKey(key)) {
                        nouvelleValeur += this.ressources.get(key) * pourcentageParRessources.get(key)/100;
                    }
                }
                Map<String, Double> quantiteParRessources = ressourceBonus.getQuantiteParRessources();
                for (String key : quantiteParRessources.keySet()) {
                    if (this.ressources.containsKey(key)) {
                        nouvelleValeur += this.ressources.get(key) * quantiteParRessources.get(key);
                    }
                }

                long val = this.ressources.get(ressource) + nouvelleValeur.longValue();
                if(bonus.containsKey("Max-"+ressource)){
                    if(val>=bonus.get("Max-"+ressource).getQuantite()){
                        val = (long)bonus.get("Max-"+ressource).getQuantite();
                    }
                }
                this.ressources.replace(keyBonus, val);
            }
        }
    }

    public String getNom() {
        return nom;
    }

    public String getCivilisation() {
        return civilisation;
    }

    public long getTempsDeJeu() {
        return tempsDeJeu;
    }

    public long getTempsDemarrage() {
        return tempsDemarrage;
    }

    public long getDebutDeLaPartie() {
        return debutDeLaPartie;
    }

    public HashMap<String, Long> getRessources() {
        return ressources;
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
}
