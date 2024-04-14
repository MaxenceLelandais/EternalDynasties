import { Component } from '@angular/core';
import { Environnement } from '../model/environnement.model';
import { EnvironnementService } from '../service/environnementService';

@Component({
  selector: 'app-musique',
  templateUrl: './musique.component.html',
  styleUrls: ['./musique.component.scss']
})
export class MusiqueComponent {
  isPaused: boolean = true;

  togglePause(): void {
    this.isPaused = !this.isPaused;
  }
}
