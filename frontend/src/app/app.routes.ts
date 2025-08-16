import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { Login } from './auth/login/login';

export const routes: Routes = [
    { path: 'inicio', component: Dashboard },
    { path: '', redirectTo: '/inicio', pathMatch: 'full' },
    { path: 'iniciar-sesion', component: Login },
    { path: '**', redirectTo: '/inicio' }
];
