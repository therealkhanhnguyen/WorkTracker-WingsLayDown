import { Routes } from '@angular/router';
import { DashboardComponent } from './feature/dashboard/dashboard.component';
import { authGuard } from './auth/auth.guard';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
     { path: '', component: DashboardComponent },
     
     { path: 'login', component: LoginComponent },

  // protect dashboard route
  { path: '', component: DashboardComponent, canActivate: [authGuard] },

  { path: '**', redirectTo: '' }
];
