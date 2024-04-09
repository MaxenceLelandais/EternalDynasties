import { Component } from '@angular/core';
import { Ere, Eres } from 'src/app/model/ere.model';
import { JeuService } from 'src/app/http/jeuService';
import { NomJoueurService } from 'src/app/service/nomJoueurService';


@Component({
  selector: 'app-frise-chronologique',
  templateUrl: './frise-chronologique.component.html',
  styleUrls: ['./frise-chronologique.component.scss']
})
export class FriseChronologiqueComponent {
  listeEre: Eres | null = null;
  nomJoueur: string | null = null;
  ere: Ere | null = null;

  constructor(private jeuService: JeuService, private nomJoueurService: NomJoueurService) {}

  ngOnInit() {
    this.nomJoueur = this.nomJoueurService.getNomJoueur();
    this.fetchData()
    console.log("nom pour ere : " + this.nomJoueur);
  }

  fetchData() {
    this.jeuService.httpListeEres().subscribe(
      data => {
        this.listeEre = data["Ere"];
        console.log(data);
      },
      error => {
        console.error("Erreur lors de la récupération des eres", error);
      }
    );
    console.log("bonjour0 : " + this.nomJoueur);
    if (this.nomJoueur) {
      this.jeuService.httpEre(this.nomJoueur).subscribe({
        next: (response) => {
          this.ere = response;
          console.log('Réponse ere', response);
        },
        error: (error) => {
          console.error('Erreur lors de la requête', error);
        }
      });
    }
  }
}

