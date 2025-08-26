import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { Login } from './auth/login/login';

import { CanActivateFn } from '@angular/router';

// Guard temporal para decidir si mostrar login o dashboard
const inicioGuard: CanActivateFn = (route, state) => {
    // Aquí deberías inyectar el servicio de login y devolver true/false según el estado
    // Por ahora, siempre redirige al login
    return false;
};

export const routes: Routes = [
    { path: 'inicio', component: Dashboard },
    { path: '', component: Login, canActivate: [inicioGuard] },
    { path: 'iniciar-sesion', component: Login },
    { path: '**', redirectTo: '/inicio' }
];
