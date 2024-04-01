export interface Ere {
    id:number,
    nom: string;
    description: string;
  }
  
  export interface Eres {
    [key: string]: Ere;
  }
