import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { LoginComponent } from './login/login.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {

  @ViewChild(MatSidenav, { static: true })
  private drawer: MatSidenav;

  ngOnInit(): void {
    const url = 'api/users/hello';
    this.drawer.open();
  }

  onActivate(component: any): void {
    if (component instanceof LoginComponent) {
      this.drawer.close();
    }
  }

  onDeactivate(component: any): void {
    if (component instanceof LoginComponent) {
      this.drawer.open();
    }
  }

}
