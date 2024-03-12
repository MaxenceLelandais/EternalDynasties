import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Environnement } from 'src/app/model/environnement.model';

@Injectable({
  providedIn: 'root'
})
export class EnvironnementService {
  private environnementSource = new BehaviorSubject<Environnement | null>(null);
  currentEnvironnement = this.environnementSource.asObservable();

  constructor() { }

  changeEnvironnement(environnement: Environnement) {
    this.environnementSource.next(environnement);
  }
}
