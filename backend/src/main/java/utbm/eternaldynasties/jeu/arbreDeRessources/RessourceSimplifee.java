package utbm.eternaldynasties.jeu.arbreDeRessources;

public class RessourceSimplifee {
    public String nom = "";
    public double quantite = 0.0;
    public double max = 0.0;
    public String type = "";
    public int id = -1;
    public String image = "";

    public double valeurEchange = -1;

    public RessourceSimplifee(String nom, double quantite, double max, String type, int id, String image, double valeurEchange){
        this.nom = nom;
        this.quantite = quantite;
        this.max = max;
        this.type = type;
        this.id = id;
        this.image = image;
        this.valeurEchange = valeurEchange;
    }
}
