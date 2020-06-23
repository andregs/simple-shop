import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class HasRoleGuard implements CanActivate {
  constructor(
    private readonly authService: AuthService,
  ) { }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    console.debug('cheking', route.data.role);

    return this.authService.hasRole(route.data.role).pipe(
      switchMap(authorized => {
        if (authorized) {
          return of(true);
        }

        return throwError('Forbidden');
      }),
    );
  }
}
