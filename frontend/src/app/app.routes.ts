import { Routes, Router, CanActivateFn } from '@angular/router';
import { map } from 'rxjs/operators';
import { Login } from './auth/login/login';
import { inject } from '@angular/core';
import { LoginService } from './services/auth/login.service';
import { Home } from './shared/pages/home/home';
import { Profile } from './shared/pages/profile/profile';
import { Signup } from './auth/signup/signup';
import { AdminList } from './admin/admin-list/admin-list';
import { CreateAdmin } from './admin/create-admin/create-admin';

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

const adminGuard: CanActivateFn = (route, state) => {
    const loginService = inject(LoginService);
    const router = inject(Router);
    const isLoggedIn = loginService.currentIsUserLoggedIn.getValue();
    if (isLoggedIn) {
        return loginService.userIsAdmin.pipe(
            map((isAdmin: boolean) => isAdmin ? true : router.parseUrl('/inicio'))
        );
    } else {
        return router.parseUrl('/iniciar-sesion');
    }
}

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
    { path: 'registro', component: Signup, canActivate: [guestGuard] },
    { path: 'perfil', component: Profile, canActivate: [authGuard] },
    { path: 'administradores', component: AdminList, canActivate: [adminGuard] },
    { path: 'administradores/crear', component: CreateAdmin, canActivate: [adminGuard] },
    { path: '**', redirectTo: '/inicio' }
];
