import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { UserService } from './user.service';

@Injectable({ providedIn: 'root' })
export class UserListResolver implements Resolve<User[]> {

  constructor(
    private readonly userService: UserService,
  ) { }

  resolve(): Observable<User[]> {
    return this.userService.findAll();
  }

}
