import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LoginService {

  private output = new ReplaySubject<LoginEvent>(1);

  readonly events = this.output.asObservable();

  constructor() {
    this.send(new Unauthenticated());
  }

  init() {
    this.send(new LoginInitiated());
  }

  login(username: string) {
    this.send(new Authenticated(username));
  }

  cancel() {
    this.send(new LoginCancelled());
  }

  logout() {
    this.send(new Unauthenticated());
  }

  private send(event: LoginEvent) {
    console.info('sent', event.constructor.name);
    this.output.next(event);
  }

}

export abstract class LoginEvent { }

export class LoginInitiated extends LoginEvent { }

export class Authenticated extends LoginEvent {
  constructor(readonly username: string) {
    super();
  }
}

export class LoginCancelled extends LoginEvent { }

export class Unauthenticated extends LoginEvent { }
