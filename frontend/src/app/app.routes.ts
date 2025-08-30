import { Routes, Router, CanActivateFn } from '@angular/router';
import { Login } from './auth/login/login';
import { inject } from '@angular/core';
import { LoginService } from './services/auth/login.service';
import { Home } from './shared/home/home';
import { AdminProfile } from './admin/admin-profile/admin-profile';

// START Generado con GitHub Copilot Chat Extension

const authGuard: CanActivateFn = (route, state) => {
    const loginService = inject(LoginService);
    const router = inject(Router);
    const isLoggedIn = loginService.currentIsUserLoggedIn.getValue();
    if (isLoggedIn) {
        return true;
    } else {
        return router.parseUrl('/iniciar-sesion');
    }
};

const guestGuard: CanActivateFn = (route, state) => {
    const loginService = inject(LoginService);
    const router = inject(Router);
    const isLoggedIn = loginService.currentIsUserLoggedIn.getValue();
    if (isLoggedIn) {
        return router.parseUrl('/inicio');
    } else {
        return true;
    }
};

// END Generado con GitHub Copilot Chat Extension

export const routes: Routes = [
    { path: 'inicio', component: Home, canActivate: [authGuard] },
    { path: '', redirectTo: '/inicio', pathMatch: 'full' },
    { path: 'iniciar-sesion', component: Login, canActivate: [guestGuard] },
    { path: 'perfil', component: AdminProfile, canActivate: [authGuard] },
    { path: '**', redirectTo: '/inicio' }
];
