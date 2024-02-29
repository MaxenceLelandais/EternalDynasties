import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Demo';
  greeting = "";
  apiUrl = "api/";
  
  constructor(private http: HttpClient) {
    let tokenUrl2 = this.apiUrl+'etat';
    http.get(tokenUrl2, { observe: 'body', responseType: 'text'}).subscribe(data => {this.greeting = data; console.log(data)});
  }
}
