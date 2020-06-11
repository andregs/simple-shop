import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { flatMap, tap } from 'rxjs/operators';
import { Credentials, User } from '../model/user';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly currentUserSubject = new BehaviorSubject<User>(null);
  public readonly currentUser: Observable<User>;

  constructor(
    private readonly http: HttpClient,
  ) {
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public login({ username, password }: Credentials): Observable<User> {
    const token = window.btoa(`${username}:${password}`);
    const authorization = `Basic ${token}`;
    const headers = new HttpHeaders({ authorization });

    console.info('login', username, password, token);

    return this.http.get<User>('api/users/current', { headers }).pipe(
      tap(user => {
        console.debug('GET current user', user);
        this.currentUserSubject.next(user);
      }),
      flatMap(user => this.currentUser),
    );
  }

}
