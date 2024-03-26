import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';

@Component({
  selector: 'app-batiments',
  templateUrl: './batiments.component.html',
  styleUrls: ['./batiments.component.scss']
})
export class BatimentsComponent {

  valClick!:Map<string, string>;

  @Input()
  donnees!:Ressources;

  @Input()
  quantite!:any;

  @Output() addFonction: EventEmitter<any> = new EventEmitter<any>();

  constructor(){
    this.valClick = new Map<string, string>;
  }

  add(nom:string){
    
    let nombre = 0;
    if(!this.valClick.has(nom)){
      this.valClick.set(nom,"1");
    }
    
    switch(this.valClick.get(nom)){
      case "1":{
        nombre=1;
        break;
      }
      case "10":{
        nombre=10;
        break;
      }
      case "100":{
        nombre=100;
        break;
      }
      default :{
        nombre=10000000;
        break;
      }
    }
    this.quantite[nom] = this.quantite[nom] + nombre;
    this.addFonction.emit(nom);
  }

  getNumberRessources(nom:string){
    return this.quantite[nom];
  }

  activate(idPere:string, idEnfant:string){

    this.changeClass(idPere,"1","multiplicateur");
    this.changeClass(idPere,"10","multiplicateur");
    this.changeClass(idPere,"100","multiplicateur");
    this.changeClass(idPere,"OO","multiplicateur");
    this.changeClass(idPere,idEnfant,"multiplicateur activate");
    this.valClick.set(idPere,idEnfant);
  }

  changeClass(idPere:string, idEnfant:string, classe:string){
    let elementFils = document.getElementById(idPere+"-"+idEnfant);
    if(elementFils!=null){
      elementFils.className = classe;
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
