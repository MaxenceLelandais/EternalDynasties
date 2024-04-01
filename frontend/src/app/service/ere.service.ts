import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Eres } from '../model/ere.model'; // Import de l'interface Ere

@Injectable({
  providedIn: 'root'
})
export class EreService {
  private ereActuelleSource = new BehaviorSubject<string | null>(null);
  currentEreActuelle = this.ereActuelleSource.asObservable();

  constructor(private http: HttpClient) { }

  updateEreActuelle(ereActuelle: string) {
    this.ereActuelleSource.next(ereActuelle);
  }

  getEreActuelleFromServer(): Observable<string> {
    // Effectuer une requête HTTP pour récupérer l'ère actuelle depuis le serveur
    return this.http.get<string>('http://localhost:9876/jeu/ereActuelle');
  }

  // Méthode pour récupérer la liste des Eres
  getEresFromServer(): Observable<Eres> {
    return this.http.get<Eres>('http://localhost:9876/jeu/ereActuelle?nomJoueur=maxen');
  }
}
