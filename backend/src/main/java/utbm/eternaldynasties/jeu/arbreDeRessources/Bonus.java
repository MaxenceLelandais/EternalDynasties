package utbm.eternaldynasties.jeu.arbreDeRessources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bonus {
    private String ressourceGenere;
    private double quantite;
    private double quantiteParSecondes;
    private Map<String, Double> quantiteParRessources = new HashMap<>();
    private double pourcentage;
    private double pourcentageParSecondes;
    private Map<String, Double> pourcentageParRessources = new HashMap<>();

    public Bonus(String ressourceGenere, String generation) {
        this.ressourceGenere = ressourceGenere;
        for (String production : generation.split(";")) {
            // par
            if (production.contains("/")) {
                if (production.contains("%/s")) {
                    this.pourcentageParSecondes = convertStringToDouble(production.replace("%/s", ""));
                } else if (production.contains("/s")) {
                    this.quantiteParSecondes = convertStringToDouble(production.replace("/s", ""));
                } else if (production.contains("%/")) {
                    String[] data = production.split("%/");
                    this.pourcentageParRessources.put(data[1], convertStringToDouble(data[0]));
                } else {
                    String[] data = production.split("/");
                    this.quantiteParRessources.put(data[1], convertStringToDouble(data[0]));
                }
            }
            // %
            else if (production.contains("%")) {
                this.pourcentage = convertStringToDouble(production.replace("%", ""));
            } else {
                this.quantite = convertStringToDouble(production);
            }
        }
    }

    private Double convertStringToDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (IllegalArgumentException e) {
            return 0.0;
        }
    }

    public String getRessourceGenere() {
        return ressourceGenere;
    }

    public double getQuantite() {
        return quantite;
    }

    public double getQuantiteParSecondes() {
        return quantiteParSecondes;
    }

    public Map<String, Double> getQuantiteParRessources() {
        return quantiteParRessources;
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

    public void add(Bonus bonus) {
        this.quantite += bonus.getQuantite();
        this.quantiteParSecondes += bonus.getQuantiteParSecondes();
        for (String key : bonus.getQuantiteParRessources().keySet()) {
            if (this.quantiteParRessources.containsKey(key)) {
                this.quantiteParRessources.replace(key, this.quantiteParRessources.get(key) + bonus.getQuantiteParRessources().get(key));
            } else {
                this.quantiteParRessources.put(key, bonus.getQuantiteParRessources().get(key));
            }
        }
        this.pourcentage += bonus.getPourcentage();
        this.pourcentageParSecondes += bonus.getPourcentageParSecondes();
        for (String key : bonus.getPourcentageParRessources().keySet()) {
            if (this.pourcentageParRessources.containsKey(key)) {
                this.pourcentageParRessources.replace(key, this.pourcentageParRessources.get(key) + bonus.getPourcentageParRessources().get(key));
            } else {
                this.pourcentageParRessources.put(key, bonus.getPourcentageParRessources().get(key));
            }
        }
    }

    public void moins(Bonus bonus) {
        this.quantite -= bonus.getQuantite();
        this.quantiteParSecondes -= bonus.getQuantiteParSecondes();
        for (String key : bonus.getQuantiteParRessources().keySet()) {
            if (this.quantiteParRessources.containsKey(key)) {
                this.quantiteParRessources.replace(key, this.quantiteParRessources.get(key) - bonus.getQuantiteParRessources().get(key));
            } else {
                this.quantiteParRessources.put(key, -bonus.getQuantiteParRessources().get(key));
            }
        }
        this.pourcentage -= bonus.getPourcentage();
        this.pourcentageParSecondes -= bonus.getPourcentageParSecondes();
        for (String key : bonus.getPourcentageParRessources().keySet()) {
            if (this.pourcentageParRessources.containsKey(key)) {
                this.pourcentageParRessources.replace(key, this.pourcentageParRessources.get(key) - bonus.getPourcentageParRessources().get(key));
            } else {
                this.pourcentageParRessources.put(key, -bonus.getPourcentageParRessources().get(key));
            }
        }
    }

    public String toString() {
        ArrayList<String> text = new ArrayList<>();
        if (this.getQuantite() != 0.0) {
            text.add(this.getQuantite() + "");
        }

        if (this.getQuantiteParSecondes() != 0.0) {
            text.add(this.getQuantiteParSecondes() + "/s");
        }
        if (this.getPourcentage() != 0.0) {
            text.add(this.getPourcentage() + "%");
        }
        if (this.getPourcentageParSecondes() != 0.0) {
            text.add(this.getPourcentageParSecondes() + "%/s");
        }

        for (String key : this.getQuantiteParRessources().keySet()) {
            text.add(this.getQuantiteParRessources().get(key) + "/" + key);
        }
        for (String key : this.getPourcentageParRessources().keySet()) {
            text.add(this.getPourcentageParRessources().get(key) + "%/" + key);
        }
        return String.join(";", text);
    }

    public void addQuantite(double quantite) {
        this.quantite+=quantite;
    }
}
