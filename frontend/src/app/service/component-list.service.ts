import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IconService {
  private iconSource = new BehaviorSubject<any>(null);
  currentIcon = this.iconSource.asObservable();

  constructor() { }

  changeIcon(icon: any) {
    this.iconSource.next(icon);
  }
}
