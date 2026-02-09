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
    // store creds
    this.auth.login(this.username.trim(), this.password);

    // Optional: lightweight “verify” by calling backend later.
    // For now, redirect and let API calls succeed/fail naturally.
    this.router.navigateByUrl('/');
  }
}
