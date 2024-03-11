package utbm.eternaldynasties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utbm.eternaldynasties.jeu.Jeu;
import utbm.eternaldynasties.jeu.Joueur;
import utbm.eternaldynasties.jeu.arbreDeRessources.Ressource;
import utbm.eternaldynasties.jeu.arbreRecherches.Recherche;
import utbm.eternaldynasties.services.JeuService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JeuTest {
    @Autowired
    private JeuService jeuService;
    String civilisation = "Test";
    String environnement = "Plaine";
    int nbrCliquesParSecondes = 5;

    @Test
    void contextLoads() {
        Jeu jeu = this.jeuService.getJeu();

        Joueur joueur = jeu.startPartie(civilisation, environnement);

        Map<String, Recherche> allRecherches = joueur.getArbreDeRecherche().getListeRecherches();
        Map<String, Ressource> allRessources = joueur.getArbreDeRessources().getListeRessources();

        List<Ressource> ressourcesSansCout = allRessources.values().stream().filter(ressource -> ressource.getListeCout().isEmpty()).toList();

        HashMap<String, Long> listeRessourcePossedee = joueur.getRessources();

        while (!joueur.recherchesPossibles().isEmpty()) {
            List<Recherche> listeRecherchesPossibles = joueur.recherchesPossibles();
            Recherche rechercheLaMoinsCouteuse = null;
            long quantiteDeRessourcesAOptenirLaMoinsNombreuse = 0L;

            for (Recherche rechercheActuelleAComparer : listeRecherchesPossibles) {
                long quantiteAOptenir = 0L;
                for (String nomRessource : rechercheActuelleAComparer.getListeCout().keySet()) {
                    long quantiteDeRessourcesAOptenir = rechercheActuelleAComparer.getListeCout().get(nomRessource) - listeRessourcePossedee.get(nomRessource);
                    quantiteAOptenir += quantiteDeRessourcesAOptenir > 0 ? quantiteDeRessourcesAOptenir : 0;
                }
                if (quantiteDeRessourcesAOptenirLaMoinsNombreuse == 0L) {
                    quantiteDeRessourcesAOptenirLaMoinsNombreuse = quantiteAOptenir;
                    rechercheLaMoinsCouteuse = rechercheActuelleAComparer;
                } else {
                    if (quantiteDeRessourcesAOptenirLaMoinsNombreuse > quantiteAOptenir) {
                        quantiteDeRessourcesAOptenirLaMoinsNombreuse = quantiteAOptenir;
                        rechercheLaMoinsCouteuse = rechercheActuelleAComparer;
                    }
                }
            }

            double tempsCollectRessources = 0.0;
            if (rechercheLaMoinsCouteuse != null) {
                System.out.println("Nom de la recherche : " + rechercheLaMoinsCouteuse.getNom());
                for (String nomRessource : rechercheLaMoinsCouteuse.getListeCout().keySet()) {
                    tempsCollectRessources += 1;
                    long quantiteDeRessourcesAOptenir = rechercheLaMoinsCouteuse.getListeCout().get(nomRessource) - listeRessourcePossedee.get(nomRessource);
                    long nombreRessourcesAOptenir = quantiteDeRessourcesAOptenir > 0 ? quantiteDeRessourcesAOptenir : 0;
                    System.out.println(nomRessource + " manquant : " + nombreRessourcesAOptenir);
                    for (int nbrClick = 0; nbrClick < nombreRessourcesAOptenir; nbrClick++) {
                        joueur.clickAchat(nomRessource);
                        tempsCollectRessources += 1.0 / this.nbrCliquesParSecondes;
                    }
                }
                joueur.activerRecherche(rechercheLaMoinsCouteuse.getNom());
                System.out.println("Temps collecte ressources : " + tempsCollectRessources);
            }
        }
    }
}
