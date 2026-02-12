import { TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

describe('LoginComponent (Unit Tests)', () => {
  let authSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authSpy = jasmine.createSpyObj<AuthService>('AuthService', ['login', 'logout', 'getAuthHeader'], {
      isLoggedIn: () => false
    } as any);

    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigateByUrl']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authSpy },
        { provide: Router, useValue: routerSpy },
      ],
    }).compileComponents();
  });

  it('TC-LOGIN-01: should show error when password is missing', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    const comp = fixture.componentInstance;

    comp.username = 'shopfloor';
    comp.password = ''; // missing password

    comp.onLogin();

    expect(comp.error).toBe('Password is required.');
    expect(authSpy.login).not.toHaveBeenCalled();
    expect(routerSpy.navigateByUrl).not.toHaveBeenCalled();
  });

  it('TC-LOGIN-02: should login and navigate when username and password are provided', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    const comp = fixture.componentInstance;

    comp.username = 'shopfloor';
    comp.password = 'shopfloor123';

    comp.onLogin();

    expect(comp.error).toBe('');
    expect(authSpy.login).toHaveBeenCalledWith('shopfloor', 'shopfloor123');
    expect(routerSpy.navigateByUrl).toHaveBeenCalledWith('/');
  });
});
