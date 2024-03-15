import { Component } from '@angular/core';

@Component({
  selector: 'app-menu-burger',
  templateUrl: './menu-burger.component.html',
  styleUrls: ['./menu-burger.component.scss']
})
export class MenuBurgerComponent {

  isMenuOpen: boolean = false; // Ajout de la variable pour contrôler l'état du menu

  listeBoutons = [
    {"src":"assets\\img\\burger\\coli.png","class":"coli"},
    {"src":"assets\\img\\burger\\vu.png","class":"vu"},
    {"src":"assets\\img\\burger\\home.png","class":"home"},
    {"src":"assets\\img\\burger\\metier.png","class":"metier"},
    {"src":"assets\\img\\burger\\ressource.png","class":"ressource"},
    {"src":"assets\\img\\burger\\puissance.png","class":"puissance"}
  ]

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }
}
