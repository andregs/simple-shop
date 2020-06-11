import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class XhrInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.debug('xhr', req.method, req.url);

    // this conventional header makes spring answer to unauthenticated requests with 401,
    // without it, response would be 302
    const setHeaders = { 'X-Requested-With': 'XMLHttpRequest' };

    const newReq = req.clone({ setHeaders });
    return next.handle(newReq);
  }

}
