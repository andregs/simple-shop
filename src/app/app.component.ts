import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(
    private http: HttpClient,
    private breakpointObserver: BreakpointObserver
  ) {
  }

  ngOnInit(): void {
    const url = 'api/users/hello';
    // http.get<any>(url).subscribe(
    //   data => this.greeting = data,
    // );
  }

}
