import { Component, OnInit } from '@angular/core';
import { Civilisation } from 'src/app/model/civilisation.model';
import { CivilisationService } from 'src/app/service/civilisationService';
import { JeuService } from 'src/app/http/jeuService';
import { Environnement } from 'src/app/model/environnement.model';

@Component({
  selector: 'app-page-jeu',
  templateUrl: './page-jeu.component.html',
  styleUrls: ['./page-jeu.component.css']
})
export class PageJeuComponent implements OnInit {
  civilisation: Civilisation | null = null;
  environnement: Environnement | null = null;
  

  constructor(private jeuService: JeuService, private civilisationService: CivilisationService) { }

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
            console.log(data);
          },
          error => {
            console.error("Erreur lors de la récupération des environnements", error);
          }
        );
    }
  }
  
}
