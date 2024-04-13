import { Component, OnInit } from '@angular/core';
import { Environnement } from 'src/app/model/environnement.model';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { ModalService } from 'src/app/service/modal.Service';
import { Ressources } from 'src/app/model/ressource.model';
import { RessourceService } from 'src/app/service/ressourceService';
import { Subscription } from 'rxjs';
import { JeuService } from 'src/app/http/jeuService';

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
    private civilisationService: CivilisationService, 
    private modalService: ModalService,
    private jeuService: JeuService,
    private ressourcesService: RessourceService) {}


  
  ngOnInit() {
    this.subscriptions.add(this.ressourcesService.ressources$.subscribe(
      ressources => this.ressources = ressources
    ));
    if(this.civilisationService.getCivilisation()!=null){
      this.civilisation = this.civilisationService.getCivilisation();
    }
    this.loadEnvironnement();
    this.loadRessources();
  }

  openModal() {
    this.modalService.open();
  }

  public loadEnvironnement() {
    if(this.civilisation!=null){
      this.jeuService.httpEnvironnement(this.civilisation.nomEnvironnement).subscribe({
        next: (reponse) => {
          this.environnement = reponse;
        },
        error: (error) => {
          console.error('Erreur lors de la requête', error);
        }
      });
    }
  }

  public loadRessources() {
    if(this.civilisation!=null){
        this.jeuService.httpListeRessources(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement).subscribe({
          next: (reponse) => {
            this.ressources = reponse;
            if(this.ressources!=null)
            {
              this.ressourcesService.updateRessources(this.ressources);
              this.subscriptions.add(
                this.ressourcesService.ressources$.subscribe(ressources => {
                  this.ressources = ressources;
                })
              )
              localStorage.setItem('ressources', JSON.stringify(this.ressources));
            }
          },
          error: (error) => {
            console.error('Erreur lors de la requête', error);
          }
        });
    }
  }
  
  updateResources(newResource: Ressources) {
    this.ressources = newResource;
  }
}