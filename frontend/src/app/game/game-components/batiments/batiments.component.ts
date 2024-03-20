import { Component, Input } from '@angular/core';
import { Ressources } from 'src/app/model/ressource.model';

@Component({
  selector: 'app-batiments',
  templateUrl: './batiments.component.html',
  styleUrls: ['./batiments.component.scss']
})
export class BatimentsComponent {

  valClick!:string;

  @Input()
  donnees!:Ressources;

  @Input()
  quantite!:any;

  constructor(){
    this.valClick = "1";
  }

  add(nom:string){
    
    let nombre = 0;

    switch(this.valClick){
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

  activate(id:string){
    this.changeClass("1","multiplicateur");
    this.changeClass("10","multiplicateur");
    this.changeClass("100","multiplicateur");
    this.changeClass("OO","multiplicateur");
    this.changeClass(id,"multiplicateur activate");
    this.valClick = id;
  }

  changeClass(id:string, classe:string){
    let element = document.getElementById(id);
    if(element!=null){
      element.className = classe;
    }
  }
}
