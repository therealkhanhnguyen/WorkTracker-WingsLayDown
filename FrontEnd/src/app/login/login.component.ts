import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  username = '';
  password = '';
  error = '';
  constructor(private auth: AuthService, private router: Router) {}

onLogin() {
  this.error = '';

  const u = this.username.trim();
  const p = this.password;

  if (!u) {
    this.error = 'Username is required.';
    return;
  }

  if (!p) {
    this.error = 'Password is required.';
    return;
  }

  this.auth.login(u, p);
  this.router.navigateByUrl('/');
}

}
