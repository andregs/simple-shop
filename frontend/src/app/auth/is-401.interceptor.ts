import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { from, Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { URLS } from '../model/constants';

@Injectable()
export class Is401Interceptor implements HttpInterceptor {

  constructor(
    private readonly router: Router,
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<any> {
    console.info('running our interceptor');

    return next.handle(req).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          if (req.url === URLS.CURRENT_USER) {
            return of(new HttpResponse({ body: null, status: 204 }));
          } else {
            return from(this.router.navigate(['/login']));
          }
        }

        return throwError(error);
      }),
    );
  }

}
