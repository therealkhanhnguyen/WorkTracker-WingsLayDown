import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly KEY = 'worktracker_basic_auth';

  // signal so UI/guards can react
  private _isLoggedIn = signal<boolean>(this.hasStoredCreds());
  isLoggedIn = this._isLoggedIn.asReadonly();

  login(username: string, password: string) {
    const token = btoa(`${username}:${password}`);
    localStorage.setItem(this.KEY, token);
    this._isLoggedIn.set(true);
  }

  logout() {
    localStorage.removeItem(this.KEY);
    this._isLoggedIn.set(false);
  }

  getAuthHeader(): string | null {
    const token = localStorage.getItem(this.KEY);
    return token ? `Basic ${token}` : null;
  }

  private hasStoredCreds(): boolean {
    return !!localStorage.getItem(this.KEY);
  }
}
