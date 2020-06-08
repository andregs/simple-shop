import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  title = 'Demo';
  greeting = { id: 'XXX', content: 'Hello World' };

  constructor(
    private http: HttpClient,
    private breakpointObserver: BreakpointObserver
  ) {
    const url = 'api/users/hello';
    // http.get<any>(url).subscribe(
    //   data => this.greeting = data,
    // );
  }

}
