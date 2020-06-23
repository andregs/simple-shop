import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { from, Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class IsAnonymousGuard implements CanActivate {

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
  ) { }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.authService.checkAuthentication().pipe(
      switchMap(user => {
        if (user === null) {
          return of(true);
        }

        const destination = [route.queryParams.returnUrl || '/'];
        return from(this.router.navigate(destination));
      }),
    );
  }
}
