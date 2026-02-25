import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable, EMPTY } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {
  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    // Only handle API calls
    if (!req.url.startsWith('/api')) {
      return next.handle(req);
    }

    const header = this.auth.getAuthHeader();

    // If not logged in, DON'T call protected APIs (prevents 401 -> popup)
    if (!header) {
      // If user isn't already on /login, send them there
      if (this.router.url !== '/login') {
        this.router.navigateByUrl('/login');
      }
      return EMPTY; // cancels the request
    }

    return next.handle(req.clone({ setHeaders: { Authorization: header } }));
  }
}
