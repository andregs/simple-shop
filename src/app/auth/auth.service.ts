import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, ReplaySubject } from 'rxjs';
import { finalize, switchMap, tap, map } from 'rxjs/operators';
import { URLS } from '../model/constants';
import { Credentials, User, Role } from '../model/user';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly currentUserSubject = new ReplaySubject<User | null>(1);
  readonly currentUser: Observable<User | null>;

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
  ) {
    this.currentUser = this.currentUserSubject.asObservable();
  }

  checkAuthentication(): Observable<User | null> {
    return this.http.get<User>(URLS.CURRENT_USER).pipe(
      switchMap(user => {
        // TODO find a better logging method and ban 'console' via linter
        this.currentUserSubject.next(user);
        return this.currentUser;
      }),
    );
  }

  login({ username, password }: Credentials): Observable<User> {
    console.info('login', username, password);

    const formData = new FormData();
    formData.set('username', username);
    formData.set('password', password);

    return this.http.post<User>('api/login', formData).pipe(
      tap(user => {
        console.debug('POST login');
        this.currentUserSubject.next(user);
      }),
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>('api/logout', null).pipe(
      finalize(() => {
        console.info('going home');
        this.currentUserSubject.next(null);
        this.router.navigate(['/']);
      }),
    );
  }

  hasRole(role: Role): Observable<boolean> {
    return this.currentUser.pipe(
      map(user => user != null && user.authorities.some(
        a => a.authority === role,
      )),
    );
  }

}
