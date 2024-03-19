import { LesBonus } from "./bonus.model";

export interface Ressource {
    nom: string;
    description: string;
    icone: string;
    cout: string;
    bonus: string;
    listeBonus: LesBonus[];
    listeCout: LesCouts;
  }
  
  export interface Ressources {
    [key: string]: Ressource;
  }
  export interface LesCouts {
    [key: string]: number;
  }

