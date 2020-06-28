import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { URLS } from '../model/constants';
import { User } from '../model/user';

@Injectable({ providedIn: 'root' })
export class UserService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  findAll(): Observable<User[]> {
    return this.http.get<User[]>(URLS.USERS);
  }

}
