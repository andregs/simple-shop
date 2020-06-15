import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, of, ReplaySubject, throwError } from 'rxjs';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { Credentials, User } from '../model/user';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly currentUserSubject = new ReplaySubject<User | null>(1);
  readonly currentUser: Observable<User | null>;

  constructor(
    private readonly http: HttpClient,
  ) {
    this.currentUser = this.currentUserSubject.asObservable();
  }

  checkAuthentication(): Observable<never> {
    return this.http.get<User>('api/users/current').pipe(
      catchError(e => this.handle401(e)),
      switchMap(user => {
        // TODO find a better logging method and ban 'console' via linter
        console.debug('auth check', user);
        this.currentUserSubject.next(user);
        return EMPTY;
      }),
    );
  }

  private handle401(error: any): Observable<null> {
    if (error instanceof HttpErrorResponse && error.status === 401) {
      console.debug('user is not authenticated');
      return of(null);
    } else {
      return throwError(error);
    }
  }

  login({ username, password }: Credentials): Observable<void> {
    console.info('login', username, password);

    const formData = new FormData();
    formData.set('username', username);
    formData.set('password', password);

    return this.http.post<void>('api/login', formData).pipe(
      tap(() => {
        console.debug('POST login');
        this.currentUserSubject.next({ name: username });
      }),
    );
  }

  logout(): Observable<never> {
    return this.http.post<void>('api/logout', null).pipe(

      // FIXME move this to a guard before login component, because we cannot route there when already auth'd.
      // that can happen when we browse directly to http://localhost:4200/login
      // or if we hit 'back' in browser after login is ok

      // We need to hit backend to generate a new XSRF cookie. Otherwise, the next login attempt would fail.
      // This could be done differently:
      // - checkAuthentication on init login component, or
      // - with a full page reload (due to checkAuthentication on init app component), or
      // - on submit login, if we replay the request (1st failure returns new XSRF cookie);
      switchMap(() => this.checkAuthentication()),
    );
  }

}
