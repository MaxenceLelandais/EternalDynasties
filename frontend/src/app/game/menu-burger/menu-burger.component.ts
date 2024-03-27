import { Component } from '@angular/core';
import { MerveilleComponent } from '../game-components/merveille/merveille.component';
import { ApercueComponent } from '../game-components/apercue/apercue.component';
import { RessourcesComponent } from '../game-components/ressources/ressources.component';
import { MetiersComponent } from '../game-components/metiers/metiers.component';
import { BatimentsComponent } from '../game-components/batiments/batiments.component';
import { PuissancesComponent } from '../game-components/puissances/puissances.component';
import { IconService } from 'src/app/service/component-list.service';
import { Icon } from 'src/app/model/icon.model';

@Component({
  selector: 'app-menu-burger',
  templateUrl: './menu-burger.component.html',
  styleUrls: ['./menu-burger.component.scss']
})





export class MenuBurgerComponent {
  constructor(private iconService: IconService) { }


  isMenuOpen = false;
  hoveredIndex = -1;
  icons = [
   {src:'../../../assets/img/icon/merveille.png', alt:'Icon 6',description: 'Merveilles',component:'MerveilleComponent'},
   {src:'../../../assets/img/icon/puissances.png', alt:'Icon 5',description: 'Puissances',component:'PuissancesComponent'},
   {src:'../../../assets/img/icon/batiments.png', alt:'Icon 4',description: 'Bâtiments',component:'BatimentsComponent'},
   {src:'../../../assets/img/icon/metiers.png', alt:'Icon 3',description: 'Métiers',component:'MetiersComponent'},
   {src:'../../../assets/img/icon/ressources.png', alt:'Icon 2',description: 'Ressources',component:'RessourcesComponent'},
   {src:'../../../assets/img/icon/apercue.png', alt:'Icon 1',description: 'Aperçue',component:'ApercueComponent'}
  ];

  onDragStart(icon: any) {
    this.iconService.changeIcon(icon);
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  hoverIn(index: number) {
    this.hoveredIndex = index;
  }

  hoverOut(index: number) {
    this.hoveredIndex = -1;
  }
  
}
