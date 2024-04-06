export interface Ere {
    id:number,
    nom: string;
    description: string;
    imageNonactif: string;
    imageActif: string;
  }
  
  export interface Eres {
    [key: string]: Ere;
  }
