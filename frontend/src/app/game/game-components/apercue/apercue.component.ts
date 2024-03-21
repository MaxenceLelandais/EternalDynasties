import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-apercue',
  templateUrl: './apercue.component.html',
  styleUrls: ['./apercue.component.scss']
})
export class ApercueComponent {
  @Input()
  data:any;
}
