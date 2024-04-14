import { Component, OnInit } from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Ressources } from 'src/app/model/ressource.model';
import { CivilisationService } from 'src/app/service/civilisationService';

@Component({
  selector: 'app-menu-ressources',
  templateUrl: './menu-ressources.component.html',
  styleUrls: ['./menu-ressources.component.scss']
})

export class MenuRessourcesComponent implements OnInit {
  civilisation!:any;
  metiers!:Ressources;
  batiments!:Ressources;
  ressources!:Ressources;
  quantite!:Map<string, number>;

  constructor(private jeuService: JeuService, private civilisationService: CivilisationService) {}

  ngOnInit(): void {
    this.quantite = new Map<string, number>;
    this.fetchDataArbreRessources();
    this.fetchQuantiteRessources();
  }

  fetchDataArbreRessources() {
    if(this.civilisationService.getCivilisation()!=null){
      this.civilisation = this.civilisationService.getCivilisation();
      this.jeuService.httpArbreRessources(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement).subscribe(
        data => {
          this.metiers = data["métiers"];
          this.batiments = data["bâtiments"];
          this.ressources = data["ressources"];
        },
        error => {
          console.error("Erreur lors de la récupération des environnements", error);
        }
      );
    }
  }

  fetchQuantiteRessources() {
    this.jeuService.httpListeRessources(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement).subscribe(
      data => {
        this.quantite = data;
      },
      error => {
        console.error("Erreur lors de la récupération des environnements", error);
      }
    );
  }

  fetchAddRessource(nom:string) {
    this.jeuService.httpAddRessource(this.civilisation.nom+"-"+this.civilisation.nomEnvironnement,nom).subscribe(
      data => {
        this.quantite = data;
      },
      error => {
        console.error("Erreur lors de l'ajout de la ressource", error);
      }
    );
  }
}