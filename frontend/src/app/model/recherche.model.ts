export interface Recherche {
    id: string;
    Nom: string;
    Description: string;
    Coût: string[];
    Débloque: string[];
    Bonus: string[];
    Dépendance: string[];
  }
  
  export interface Recherches {
    [key: string]: Recherche;
  }
  