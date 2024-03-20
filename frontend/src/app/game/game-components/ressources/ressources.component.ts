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
  quantite!:any;

  getNumberRessources(nom:string){
    return this.quantite[nom];
  }

  testPresent(nom:string){
    return this.quantite[nom]!=null;
  }

  add(nom:string){
    if(this.testPresent("Max-"+nom)){
      if(this.quantite[nom]+1<=this.quantite["Max-"+nom]){
        this.quantite[nom]+=1
      }
    }else{
      this.quantite[nom]+=1;
    }
  }
}
