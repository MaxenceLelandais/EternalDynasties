export interface Environnement {
    nom: string;
    description: string;
    logo: string;
    'logo-hover': string;
    banniere: string;
    ecusson: string;
    images: string[];
    merveille: string;
  }
  
  export interface Environnements {
    [key: string]: Environnement;
  }
