export interface Ere {
    Id: number;
    Nom: string;
    Description: string;
  }
  
export interface Eres {
    [key: string]: Ere;
}