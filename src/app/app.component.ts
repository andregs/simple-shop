import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'Demo';
  greeting = { id: 'XXX', content: 'Hello World' };

  constructor(private http: HttpClient) {
    const url = 'api/users/hello';
    http.get<any>(url).subscribe(
      data => this.greeting = data,
    );
  }

}
