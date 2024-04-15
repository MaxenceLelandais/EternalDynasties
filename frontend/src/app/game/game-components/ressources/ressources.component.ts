import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';

@Component({
  selector: 'app-ressources',
  templateUrl: './ressources.component.html',
  styleUrls: ['./ressources.component.scss']
})
export class RessourcesComponent implements OnInit, OnDestroy {
  ressources: Ressources | null = null;
  nomJoueur: string | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private ressourcesService: RessourceService,
    private jeuService: JeuService,
    private nomJoueurService: NomJoueurService
  ) {}

  ngOnInit() {
    this.loadRessources();
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
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

  addRessource(nomJoueur: string, ressource: string) {
    if (nomJoueur == null) {
      return;
    }

    this.subscriptions.unsubscribe();
    
    // Ajoutez un nouvel abonnement
    this.subscriptions = new Subscription();
    // Ajoutez un abonnement pour la mise à jour des ressources
    this.subscriptions.add(
    
      this.jeuService.httpAddRessource(nomJoueur, ressource, "1").subscribe({
        next: (response) => {
          // Mise à jour des ressources à travers le service
          this.ressourcesService.updateRessources(response);
          this.subscribeToRessources()
          localStorage.setItem('ressources', JSON.stringify(this.ressources));
        },
        error: (error) => {
          console.error("Erreur lors de l'ajout de la ressource", error);
        }
      })
    )
  }

  private subscribeToRessources() {
    // Ajoutez un abonnement pour la récupération des ressources
    this.subscriptions.add(
      this.ressourcesService.ressources$.subscribe(ressources => {
        this.ressources = ressources;
      })
    );
  }

}
