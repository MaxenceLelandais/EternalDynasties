package utbm.eternaldynasties.jeu;

import org.json.simple.JSONObject;
import utbm.eternaldynasties.jeu.arbreDeRessources.ArbreDeRessources;
import utbm.eternaldynasties.jeu.arbreDeRessources.Bonus;
import utbm.eternaldynasties.jeu.arbreDeRessources.Ressource;
import utbm.eternaldynasties.jeu.arbreEnvironnements.Environnement;
import utbm.eternaldynasties.jeu.arbreRecherches.ArbreDeRecherches;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.utils.Json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Joueur {

    private String civilisation;
    private Environnement environnement;
    private String debutDeLaPartie;
    private HashMap<String, Long> ressources = new HashMap<>();
    private ArrayList<String> recherches = new ArrayList<>();
    private final ArbreDeRecherches arbreDeRecherche;
    private final ArbreDeRessources arbreDeRessources;

    public Joueur(JSONObject jsonObject, ArbreDeRecherches arbreDeRecherche, ArbreDeRessources arbreDeRessources, Environnement environnement) {
        this.arbreDeRecherche = arbreDeRecherche;
        this.arbreDeRessources = arbreDeRessources;
        Map data = jsonObject;
        if (data.containsKey("Civilisation")) {
            this.environnement = environnement;
            this.civilisation = data.get("Civilisation").toString();
            this.debutDeLaPartie = (String) data.get("Début de la partie");
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
        Map<String, Long> listeCout = this.arbreDeRecherche.getRecherche(nomRecherche).getListeCout();
        StringBuilder txt = new StringBuilder(nomRecherche + " : RESSOURCES INSUFISANTES { ");
        boolean pbRessource = false;
        for(String nomRessource : listeCout.keySet()){
            long val = this.ressources.get(nomRessource)-listeCout.get(nomRessource);
            if(val<0){
                txt.append(nomRessource).append(" : ").append(val).append("; ");
                pbRessource = true;
            }
        }
        if(pbRessource){
            return txt+" }";
        }
        if (this.arbreDeRecherche.activerRecherche(nomRecherche)) {
            for(String nomRessource : listeCout.keySet()){
                this.ressources.replace(nomRessource,this.ressources.get(nomRessource)-listeCout.get(nomRessource));
            }
            Map<String, Bonus> map = this.arbreDeRecherche.getRecherche(nomRecherche).getListeBonus();
            for (String key : map.keySet()) {
                Bonus bonusRessource=map.get(key);
                String nom = bonusRessource.getRessourceGenere();
                Bonus bonusInfos = this.arbreDeRessources.getRessource(nom).getListeBonus().get(nom);
                bonusInfos.add(bonusRessource);
                if(key.contains("Max-")){
                    this.ressources.putIfAbsent(key, bonusInfos.estimationValeur(this.ressources));
                }
                this.ressources.putIfAbsent(key, 0L);
            }
            save();
            return nomRecherche + ": RECHERCHE EFFECTUEE";
        }
        return nomRecherche + " : RECHERCHE NON DEBLOQUEE";
    }

    private void checkEtAddVal(Map<String, Bonus> bonus){
        bonus.forEach((key, value)->{
            long valeur = value.estimationValeur(this.ressources);
            if (this.ressources.containsKey("Max-" + key)) {
                if (this.ressources.get(key) + valeur <= this.ressources.get("Max-" + key)) {
                    this.ressources.replace(key, this.ressources.get(key) + valeur);
                }
            }else{
                this.ressources.replace(key, this.ressources.get(key) + valeur);
            }
        });
    }

    public Map<Object, Object> clickAchat(String nomRessource) {

        Map<String, Long> cout = this.arbreDeRessources.getRessource(nomRessource).getListeCout();
        Map<String, Bonus> bonus = this.arbreDeRessources.getRessource(nomRessource).getListeBonus();
        if ((cout != null && !cout.isEmpty())) {
            boolean pasAssezDeRessources = false;
            for (String key : cout.keySet()) {
                long diff = this.ressources.get(key) - cout.get(key);
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
                Double pourcentageParSecondes = (ressourceBonus.getPourcentageParSecondes()/100);

                nouvelleValeur += quantiteParSecondes + ((pourcentageParSecondes/100) * nouvelleValeur);

                Map<String, Double> pourcentageParRessources = ressourceBonus.getPourcentageParRessources();
                for (String key : pourcentageParRessources.keySet()) {
                    if (this.ressources.containsKey(key)) {
                        nouvelleValeur += this.ressources.get(key) * (pourcentageParRessources.get(key)/100);
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

    private boolean decomposition(Map<String, Long> ressourcesComplexe, ArrayList<String> listeActions) {
        if (ressourcesComplexe.isEmpty()) {
            return true;
        } else {
            for (String nom : ressourcesComplexe.keySet()) {
                listeActions.add(nom + ":" + 1);
                Map<String, Long> besoin = this.arbreDeRessources.getRessource(nom).getListeCout();
                for (String nomBesoin : besoin.keySet()) {
                    if (this.arbreDeRessources.getRessource(nomBesoin).getListeCout().isEmpty()) {
                        listeActions.add(nomBesoin + ":" + besoin.get(nomBesoin));
                    } else {
                        if (ressourcesComplexe.containsKey(nomBesoin)) {
                            ressourcesComplexe.replace(nomBesoin, ressourcesComplexe.get(nomBesoin) + besoin.get(nomBesoin));
                        } else {
                            ressourcesComplexe.put(nomBesoin, besoin.get(nomBesoin));
                        }
                    }
                }
                long valeur = ressourcesComplexe.get(nom) - 1;
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

    public ArrayList<String> besoinRessources(Map<String, Long> ressourcesDemandees) {

        Map<String, Long> ressourcesComplexe = new HashMap<>();

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
        Map<String, Long> ressourcesMax = new HashMap<>();
        int position = 0;
        while(position<listeActions.size()) {
            String[] data = listeActions.get(position).split(":");
            String nomRessource = data[0];
            long quantite = Long.parseLong(data[1]);
            Map<Bonus, Ressource> map;
            for(Bonus bonus : this.arbreDeRessources.getRessource(nomRessource).getListeBonus().values()){
                if(bonus.getRessourceGenere().contains("Max-")){
                    if(ressourcesMax.containsKey(bonus.getRessourceGenere())){
                        ressourcesMax.replace(bonus.getRessourceGenere(),ressourcesMax.get(bonus.getRessourceGenere())+bonus.estimationValeur(this.ressources));
                    }else{
                        ressourcesMax.put(bonus.getRessourceGenere(),bonus.estimationValeur(this.ressources));
                    }
                }
            }
            if (this.ressources.containsKey("Max-" + nomRessource)) {
                ressourcesMax.putIfAbsent("Max-" + nomRessource,this.ressources.get("Max-" + nomRessource));
                if (quantite > ressourcesMax.get("Max-" + nomRessource)) {
                    map = this.arbreDeRessources.getListeRessourceEnFonctionBonus("Max-" + nomRessource, this.ressources);
                    long valeur = 0;
                    Ressource ressourceAPrendre = null;
                    for(Bonus bonus : map.keySet()){
                        long tmp = bonus.estimationValeur(this.ressources);
                        if(valeur==0){
                            valeur = tmp;
                            ressourceAPrendre = map.get(bonus);
                        }else{
                            if(valeur<tmp){
                                valeur = tmp;
                                ressourceAPrendre = map.get(bonus);
                            }
                        }
                    }
                    if(ressourceAPrendre!=null){
                        long nbrRessources = (long)((double)(quantite-ressourcesMax.get("Max-" + nomRessource))/(double)valeur+0.5);
                        HashMap<String, Long> demande = new HashMap<>();
                        demande.put(ressourceAPrendre.getNom(), nbrRessources);
                        ArrayList<String> aRajouter = this.besoinRessources(demande);
                        listeActions.addAll(position,aRajouter);
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

    public Map<Object, Object> toMap() {
        Map<Object, Object> data = new HashMap<>();
        data.put("Civilisation", this.civilisation);
        data.put("Environnement", this.environnement.getNom());
        data.put("Début de la partie", this.debutDeLaPartie + "");
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
