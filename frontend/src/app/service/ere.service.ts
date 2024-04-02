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
}
