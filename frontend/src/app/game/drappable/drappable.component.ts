import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-drappable',
  templateUrl: './drappable.component.html',
  styleUrls: ['./drappable.component.scss']
})
export class DrappableComponent {

  @Input()
  nom!:string;

  @Input()
  data!:string;

}
