import { Ere } from "./ere.model";

export interface Recherche {
    id: number;
    parent: number;
    nom: string;
    Description: string;
    icone: string;
    Coût: ValeurRecherche[];
    Bonus: ValeurRecherche[];
    debloque: Recherche[];
    verouille: Recherche[];
    ere: Ere;
    Etat: boolean;
    RecherchePossible: boolean;
  }

  export interface ValeurRecherche {
    nom: string;
    val: string;
  }
  
  export interface Recherches {
    [key: string]: Recherche;
  }
