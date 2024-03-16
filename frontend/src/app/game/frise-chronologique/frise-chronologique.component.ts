import { Component } from '@angular/core';
import { JeuService } from 'src/app/http/jeuService';
import { Eres } from 'src/app/model/ere.model';

@Component({
  selector: 'app-frise-chronologique',
  templateUrl: './frise-chronologique.component.html',
  styleUrls: ['./frise-chronologique.component.scss']
})
export class FriseChronologiqueComponent {
  listeEre: Eres | null = null;

  constructor(private jeuService: JeuService) {this.fetchData()}

  fetchData() {
    this.jeuService.httpListeEres().subscribe(
      data => {
        this.listeEre = data;
        console.log(data);
      },
      error => {
        console.error("Erreur lors de la récupération des eres", error);
      }
    );
  }
}
