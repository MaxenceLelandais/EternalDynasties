import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { JeuService } from 'src/app/http/jeuService';
import { Ressources } from 'src/app/model/ressource.model';
import { NomJoueurService } from 'src/app/service/nomJoueurService';
import { RessourceService } from 'src/app/service/ressourceService';

@Component({
  selector: 'app-metiers',
  templateUrl: './metiers.component.html',
  styleUrls: ['./metiers.component.scss']
})
export class MetiersComponent {
  ressources: Ressources | null = null;
  
  nomJoueur: string | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(private ressourcesService: RessourceService,
    private jeuService: JeuService,
    private nomJoueurService: NomJoueurService) {
  }

  ngOnInit() {
    this.subscribeToRessources();
    this.loadRessources();
    
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
  }

  private loadRessources() {
    if (localStorage.getItem('ressources')) {
      const savedRessources = localStorage.getItem('ressources');
      if (savedRessources != null) {
        this.ressources = JSON.parse(savedRessources);
      }
    } else {
      console.log("savedRessources");
      this.ressources = this.ressourcesService.getRessources();
      localStorage.setItem('ressources', JSON.stringify(this.ressources));
    }
  }

  addRessource(ressource: string) {
    if (this.nomJoueur == null) {
      return;
    }
    this.jeuService.httpAddRessource(this.nomJoueur, ressource, "1").subscribe({
      next: (response) => {
        // Mise à jour des ressources à travers le service
        this.ressourcesService.updateRessources(response);
        this.subscribeToRessources();
        localStorage.setItem('ressources', JSON.stringify(this.ressources));
      },
      error: (error) => {
        console.error("Erreur lors de l'ajout de la ressource", error);
      }
    });
  }
  private subscribeToRessources() {
    this.subscriptions.unsubscribe();
    // Ajoutez un abonnement pour la récupération des ressources
    this.subscriptions.add(
      this.ressourcesService.ressources$.subscribe(ressources => {
        this.ressources = ressources;
      })
    );
  }

  ngOnDestroy(): void {
    // Désabonnez-vous de tous les abonnements lorsque le composant est détruit
    this.subscriptions.unsubscribe();
  }
}
