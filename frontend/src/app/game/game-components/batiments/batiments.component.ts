import { Component, Input } from '@angular/core';
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
}
