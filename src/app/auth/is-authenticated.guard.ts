import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateChild, Router, RouterStateSnapshot } from '@angular/router';
import { from, Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class IsAuthenticatedGuard implements CanActivateChild {
  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
  ) { }

  canActivateChild(_: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.authService.currentUser.pipe(
      switchMap(user => {
        console.info('got user', user);
        if (user == null) {
          const returnUrl = state.url;
          console.info('going /login');
          const loginPromise = this.router.navigate(['/login'], { queryParams: { returnUrl } });
          return from(loginPromise);
        } else {
          console.info('you can pass');
          return of(true);
        }
      }),
    );
  }
}
