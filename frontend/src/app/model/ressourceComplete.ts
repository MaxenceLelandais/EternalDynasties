
export interface RessourceComplete {
    nom: string;
    description: string;
    icone: string;
    cout: string;
    bonus: string;
    listeBonusEstime: LesBonusEstimes;
    listeCout: LesCouts;
  }
  
  export interface RessourcesCompletes {
    [key: string]: RessourceComplete;
  }
  export interface LesCouts {
    [key: string]: number;
  }

  export interface LesBonusEstimes {
    [key: string]: number;
  }

