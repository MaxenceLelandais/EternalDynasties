
export interface Ressource {
    nom: string;
    description: string;
    icone: string;
    cout: string;
    bonus: string;
    listeBonusEstime: LesBonusEstimes;
    listeCout: LesCouts;
  }
  
  export interface Ressources {
    [key: string]: Ressource;
  }
  export interface LesCouts {
    [key: string]: number;
  }

  export interface LesBonusEstimes {
    [key: string]: number;
  }

