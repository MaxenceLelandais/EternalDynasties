import { Component, Input } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';

@Component({
  selector: 'app-metiers',
  templateUrl: './metiers.component.html',
  styleUrls: ['./metiers.component.scss']
})
export class MetiersComponent {
  @Input()
  donnees!:Ressources;

  @Input()
  quantite!:Map<string,number>;

  constructor(){
    console.log("MetiersComponent");
    console.log(this.donnees);
    console.log(this.quantite);
  }
}
