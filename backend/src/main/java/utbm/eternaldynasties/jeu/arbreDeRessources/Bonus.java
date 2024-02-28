package utbm.eternaldynasties.jeu.arbreDeRessources;

import java.util.HashMap;
import java.util.Map;

public class Bonus {
    private String ressourceGenere;
    private double quantite;
    private double quantiteParSecondes;
    private Map<String,Double> quantiteParRessources = new HashMap<>();
    private double pourcentage;
    private double pourcentageParSecondes;
    private Map<String,Double> pourcentageParRessources = new HashMap<>();

    public Bonus (String ressourceGenere, String generation){
        this.ressourceGenere = ressourceGenere;
        for(String production : generation.split(";")){
            // par
            if(production.contains("/")){
                if(production.contains("%/s")){
                    this.pourcentageParSecondes = convertStringToDouble(production.replace("%/s",""));
                } else if (production.contains("/s")) {
                    this.quantiteParSecondes = convertStringToDouble(production.replace("/s",""));
                }else if (production.contains("%/")) {
                    String[] data = production.split("%/");
                    this.pourcentageParRessources.put(data[1],convertStringToDouble(data[0]));
                }else{
                    String[] data = production.split("/");
                    this.quantiteParRessources.put(data[1],convertStringToDouble(data[0]));
                }
            }
            // %
            else if (production.contains("%")) {
                this.pourcentage = convertStringToDouble(production.replace("%",""));
            }else{
                this.quantite = convertStringToDouble(production);
            }
        }
    }

    private Double convertStringToDouble(String data){
        try{
            return Double.parseDouble(data);
        }catch (IllegalArgumentException e){
            return 0.0;
        }
    }

    public String getRessourceGenere() {
        return ressourceGenere;
    }

    public double getQuantite() {
        return quantite;
    }

    public void addQuantite(Double add) {
        this.quantite+=add;
    }

    public double getQuantiteParSecondes() {
        return quantiteParSecondes;
    }

    public void addQuantiteParSecondes(Double add) {
        this.quantiteParSecondes+=add;
    }

    public Map<String, Double> getQuantiteParRessources() {
        return quantiteParRessources;
    }

    public void addQuantiteParRessources(String ressource, Double add) {
        this.quantiteParRessources.get(ressource)+=add;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public double getPourcentageParSecondes() {
        return pourcentageParSecondes;
    }

    public Map<String, Double> getPourcentageParRessources() {
        return pourcentageParRessources;
    }
}
