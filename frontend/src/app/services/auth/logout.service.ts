import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { LoginService } from './login.service';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {

  private http = inject(HttpClient);
  private loginService = inject(LoginService);

  logout(): Observable<any> {
    const token = this.loginService.userToken;
    return this.http.post(environment.apiUrl + 'auth/logout', { token }).pipe(
      tap(() => {
        sessionStorage.removeItem('userData');
        this.loginService.currentUserData.next(null);
        this.loginService.currentIsUserLoggedIn.next(false);
      }),
      catchError((error) => {
        sessionStorage.removeItem('userData');
        this.loginService.currentUserData.next(null);
        this.loginService.currentIsUserLoggedIn.next(false);
        return of(error);
      })
    );
  }

}
