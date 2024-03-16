import { Component } from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Recherche, Recherches } from 'src/app/model/recherche.model';

@Component({
  selector: 'app-modal-arbre-recherche',
  templateUrl: './modal-arbre-recherche.component.html',
  styleUrls: ['./modal-arbre-recherche.component.scss']
})



export class ModalArbreRechercheComponent {
  listeRecherches: Recherches | null = null;

  constructor(private jeuService: JeuService) {this.fetchData()}

  fetchData() {
    console.log(this.listeRecherches);
    this.jeuService.httpListeRecherches().subscribe(
      data => {
        this.listeRecherches = data;
        console.log(data);
      },
      error => {
        console.error("Erreur lors de la récupération des recherches", error);
      }
    );
  }
}
