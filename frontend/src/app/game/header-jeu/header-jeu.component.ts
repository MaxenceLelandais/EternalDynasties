import { Component, OnInit } from '@angular/core';
import { Environnement } from 'src/app/model/environnement.model';
import { EnvironnementService } from 'src/app/service/environnementService';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { take } from 'rxjs/operators';
import { ModalService } from 'src/app/service/modal.Service';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header-jeu',
  templateUrl: './header-jeu.component.html',
  styleUrls: ['./header-jeu.component.scss']
})
export class HeaderJeuComponent implements OnInit {

  environnement: Environnement | null = null;
  civilisation: Civilisation | null = null;
  ressources: Ressources | null = null;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private environnementService: EnvironnementService, 
    private civilisationService: CivilisationService, 
    private modalService: ModalService,
    private ressourcesService: RessourceService) {}


  
  ngOnInit() {
    this.subscriptions.add(this.ressourcesService.ressources$.subscribe(
      ressources => this.ressources = ressources
    ));
    this.loadRessources();
   
    this.loadEnvironnement();
    this.loadCivilisation();
    
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

  openModal() {
    this.modalService.open();
    
  }

  private loadRessources() {
    if (localStorage.getItem('ressources')) {
      const savedRessources = localStorage.getItem('ressources');
      console.log("save : " + savedRessources);
      if (savedRessources != null) {
        this.ressources = JSON.parse(savedRessources);
      }
    } else {
      console.log("savedRessources");
      this.ressources = this.ressourcesService.getRessources();
      console.log("service : " + this.ressources );
      localStorage.setItem('ressources', JSON.stringify(this.ressources));
    }
  }
  
}