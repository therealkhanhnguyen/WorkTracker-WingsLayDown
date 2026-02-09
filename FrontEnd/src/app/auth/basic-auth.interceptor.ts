import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const basicAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const header = auth.getAuthHeader();

  if (!header) return next(req);

  const authReq = req.clone({
    setHeaders: { Authorization: header }
  });

  return next(authReq);
};
