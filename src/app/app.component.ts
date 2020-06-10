import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSidenav } from '@angular/material/sidenav';
import { LoginService, LoginInitiated } from './login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {

  @ViewChild(MatSidenav, { static: true })
  private drawer: MatSidenav;

  constructor(
    private http: HttpClient,
    private loginService: LoginService,
  ) {
  }

  ngOnInit(): void {
    const url = 'api/users/hello';

    this.loginService.events.subscribe(
      event => {
        console.info('got', event.constructor.name);
        if (event instanceof LoginInitiated) {
          this.drawer.close();
        } else {
          this.drawer.open();
        }
      },
    );
  }

}
