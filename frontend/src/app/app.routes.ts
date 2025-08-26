import { Routes } from '@angular/router';
import { Login } from './auth/login/login';

import { CanActivateFn } from '@angular/router';
import { Home } from './shared/home/home';

// Guard temporal para decidir si mostrar login o home
const inicioGuard: CanActivateFn = (route, state) => {
    // Aquí deberías inyectar el servicio de login y devolver true/false según el estado
    // Por ahora, siempre redirige al login
    return false;
};

export const routes: Routes = [
    { path: 'inicio', component: Home },
    { path: '', component: Login, canActivate: [inicioGuard] },
    { path: 'iniciar-sesion', component: Login },
    { path: '**', redirectTo: '/inicio' }
];
