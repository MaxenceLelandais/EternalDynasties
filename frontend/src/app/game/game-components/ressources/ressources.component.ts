import { Component, Input } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';

@Component({
  selector: 'app-ressources',
  templateUrl: './ressources.component.html',
  styleUrls: ['./ressources.component.scss']
})
export class RessourcesComponent {
  @Input()
  donnees!:Ressources;

  @Input()
  quantite!:Map<string,number>;

  constructor(){
    console.log("RessourcesComponent");
    console.log(this.donnees);
    console.log(this.quantite);
  }
}
