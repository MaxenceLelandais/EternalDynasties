import { Component, EventEmitter, Input, Output } from '@angular/core';
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

  @Output() addFonction: EventEmitter<any> = new EventEmitter<any>();
  


  getNumberRessources(nom:string){
    return this.quantite[nom];
  }

  getExclude(nom:string){
    const donneesFiltrees: Ressources = {};

    for (const key in this.donnees) {
        if (this.donnees.hasOwnProperty(key) && !this.donnees[key].nom.includes(nom)) {
            donneesFiltrees[key] = this.donnees[key];
        }
    }
    return donneesFiltrees;
  }

  testPresent(nom:string){
    return this.quantite[nom]!=null;
  }

  add(nom:string){
    if(this.testPresent("Max-"+nom)){
      if(this.quantite[nom]+1<=this.quantite["Max-"+nom]){
        this.addFonction.emit(nom);
      }
    }else{
      this.addFonction.emit(nom);
    }
  }

  detail(nom: string): string {
    const ressource = this.donnees[nom];
    let text = `Description : ${this.donnees[nom].description}`;
  
    const coutKeys = Object.keys(ressource.listeCout);
    const bonusKeys = Object.keys(ressource.listeBonusEstime);
    let presenceCout = false;
    if (coutKeys.length > 0) {
      presenceCout = true;
      text += `\n\nCoûts : \n`;
      for (const key of coutKeys) {
        text += `- ${key} : ${ressource.listeCout[key]}\n`;
      }
    }
  
    if (bonusKeys.length > 0) {
      if(presenceCout){
        text += `\nBonus : \n`;
      }else{
        text += `\n\nBonus : \n`;
      }
      
      for (const key of bonusKeys) {
        text += `- ${key} : ${ressource.listeBonusEstime[key]}\n`;
      }
    }
  
    return text;
  }

}
