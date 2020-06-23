import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AuthService } from './auth/auth.service';
import { LoginComponent } from './auth/login/login.component';
import { User, Role } from './model/user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {

  @ViewChild(MatSidenav, { static: true })
  private readonly drawer: MatSidenav;

  readonly currentUser: Observable<User | null>;

  private isLoginScreen: boolean;

  constructor(
    private readonly authService: AuthService,
  ) {
    this.currentUser = this.authService.currentUser;
  }

  ngOnInit(): void {
    this.drawer.open();
    this.authService.checkAuthentication().subscribe();
  }

  get isAuthenticated(): Observable<boolean> {
    return this.authService.currentUser.pipe(
      map(user => user != null),
    );
  }

  get currentUserName(): Observable<string> {
    return this.authService.currentUser.pipe(
      filter(user => user != null),
      map(user => user!.username),
    );
  }

  get isLoginButtonVisible(): Observable<boolean> {
    return this.isAuthenticated.pipe(
      map(authenticated => !authenticated && !this.isLoginScreen),
    );
  }

  onActivate(component: any): void {
    this.isLoginScreen = component instanceof LoginComponent;
    if (this.isLoginScreen) {
      this.drawer.close();
    }
  }

  onDeactivate(component: any): void {
    if (component instanceof LoginComponent) {
      this.drawer.open();
    }
  }

  logout(): void {
    this.authService.logout().subscribe();
  }

  hasRole(role: Role): Observable<boolean> {
    return this.authService.hasRole(role);
  }

}
