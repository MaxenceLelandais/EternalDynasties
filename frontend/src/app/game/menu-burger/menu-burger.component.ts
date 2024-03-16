import { Component } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';


@Component({
  selector: 'app-menu-burger',
  templateUrl: './menu-burger.component.html',
  styleUrls: ['./menu-burger.component.scss']
})
export class MenuBurgerComponent {
  isMenuOpen = false;
  hoveredIndex = -1;
  icons = [
    {src: "../../../assets/img/icon/merveille.png", alt: 'Icon 2', description: 'Merveilles'},
    {src: "../../../assets/img/icon/puissances.png", alt: 'Icon 2', description: 'Puissances'},
    {src: "../../../assets/img/icon/batiments.png", alt: 'Icon 4', description: 'Bâtiments'},
    {src: "../../../assets/img/icon/metiers.png", alt: 'Icon 3', description: 'Métiers'},
    {src: "../../../assets/img/icon/ressources.png", alt: 'Icon 2', description: 'Ressources'},
    {src: "../../../assets/img/icon/apercue.png", alt: 'Icon 1', description: 'Aperçue'}
  ];

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
