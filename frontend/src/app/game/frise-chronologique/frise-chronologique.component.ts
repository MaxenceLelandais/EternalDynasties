import { Component } from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Eres } from 'src/app/model/ere.model';

@Component({
  selector: 'app-frise-chronologique',
  templateUrl: './frise-chronologique.component.html',
})
export class FriseChronologiqueComponent {
  listeEre: Eres | null = null;

  constructor(private jeuService: JeuService) {this.fetchData()}

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
  }
}
