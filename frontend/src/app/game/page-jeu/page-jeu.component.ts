import { Component, OnInit } from '@angular/core';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { JeuService } from 'src/app/http/jeuService';
import { Environnement } from 'src/app/model/environnement.model';
import { HeaderJeuComponent } from '../header-jeu/header-jeu.component';
import { EnvironnementService } from 'src/app/service/environnementService';
import { MenuBurgerComponent } from '../menu-burger/menu-burger.component';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { ComponentFactoryResolver, ViewContainerRef } from '@angular/core';
import { ApercueComponent } from '../game-components/apercue/apercue.component';
import { RessourcesComponent } from '../game-components/ressources/ressources.component';
import { MetiersComponent } from '../game-components/metiers/metiers.component';
import { BatimentsComponent } from '../game-components/batiments/batiments.component';
import { PuissancesComponent } from '../game-components/puissances/puissances.component';
import { MerveilleComponent } from '../game-components/merveille/merveille.component';


@Component({
  selector: 'app-page-jeu',
  templateUrl: './page-jeu.component.html',
  styleUrls: ['./page-jeu.component.css']
})
export class PageJeuComponent implements OnInit {
  civilisation: Civilisation | null = null;
  environnement: Environnement | null = null;
  

  constructor(private jeuService: JeuService, private civilisationService: CivilisationService, private environnementService: EnvironnementService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit() {
    this.civilisation = this.civilisationService.getCivilisation();
    this.fetchData();
  }

  fetchData() {
    if (this.civilisation != null) {
      this.jeuService.httpJoueur(this.civilisation.nom, this.civilisation.nomEnvironnement).subscribe({
          next: (response) => {
            console.log('Réponse du serveur', response);
          },
          error: (error) => {
            console.error('Erreur lors de la requête', error);
          }
        });
        this.jeuService.httpEnvironnement(this.civilisation.nomEnvironnement).subscribe(
          data => {
            this.environnement = data;
            this.environnementService.changeEnvironnement(data); // Mettez à jour le service ici
            console.log(data);
          },
          error => {
            console.error("Erreur lors de la récupération des environnements", error);
          }
        );
    }
  }

  drop(event: CdkDragDrop<any>) {
    console.log(event);
    console.log(event.item.data);
    const componentData = event.item.data;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentData.component);
    this.viewContainerRef.clear();
    this.viewContainerRef.createComponent(componentFactory);
  }
}
