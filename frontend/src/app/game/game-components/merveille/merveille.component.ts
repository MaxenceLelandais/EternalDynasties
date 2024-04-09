import { Component, Input, OnInit  } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { Environnement } from 'src/app/model/environnement.model';
import { EnvironnementService } from 'src/app/service/environnementService';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { Subscription } from 'rxjs';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';

@Component({
  selector: 'app-merveille',
  templateUrl: './merveille.component.html',
  styleUrls: ['./merveille.component.scss']
})
export class MerveilleComponent {
  @Input()
  data:any;

  environnement: Environnement | null = null;
  civilisation: Civilisation | null = null;
  ressources: Ressources | null = null;
  private subscriptions: Subscription = new Subscription();
  nomJoueur: string | null = null;
  merveilleRessourceNom: string | null = null;
  

  constructor(private ressourcesService: RessourceService,
              private environnementService: EnvironnementService,
              private civilisationService: CivilisationService,
              private jeuService: JeuService,
              private nomJoueurService: NomJoueurService) {
  }

  ngOnInit() {
    this.subscriptions.add(this.ressourcesService.ressources$.subscribe(
      ressources => this.ressources = ressources
    ));
    this.loadRessources();
    this.loadEnvironnement();
    this.loadCivilisation();
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

  private loadEnvironnement() {
    if (localStorage.getItem('environnement')) {
      const savedEnv = localStorage.getItem('environnement');
      if (savedEnv != null) {
        this.environnement = JSON.parse(savedEnv);
      }
    } else {
      console.log("savedEnv");
      this.environnementService.currentEnvironnement.subscribe(environnement => {
        this.environnement = environnement;
        localStorage.setItem('environnement', JSON.stringify(environnement));
      });
    }
  }

  private loadCivilisation() {
    if (localStorage.getItem('civilisation')) {
      const savedCivilisation = localStorage.getItem('civilisation');
      if (savedCivilisation != null) {
        this.civilisation = JSON.parse(savedCivilisation);
      }
    } else {
      console.log("savedCivilisation");
      this.civilisation = this.civilisationService.getCivilisation();
      localStorage.setItem('civilisation', JSON.stringify(this.civilisation));
    }
  }

  addRessource(nomJoueur: string, ressource: string) {
    if (nomJoueur == null) {
      return;
    }
    this.jeuService.httpAddRessource(nomJoueur, ressource, "1").subscribe({
      next: (response) => {
        console.log("Ressource ajoutée avec succès", response);
        this.ressourcesService.updateRessources(response);
        this.subscriptions.add(
          this.ressourcesService.ressources$.subscribe(ressources => {
            this.ressources = ressources;
            console.log("ressources : " + this.ressources);
          })
        );
      },
      error: (error) => {
        console.error("Erreur lors de l'ajout de la ressource", error);
      }
    });
  }

}
