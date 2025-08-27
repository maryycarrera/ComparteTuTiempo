import { inject, Injectable } from '@angular/core';
import { LoginRequest } from './payload/request/login-request';
import { JwtResponse } from './payload/response/jwt-response';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError, BehaviorSubject, tap, map } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private http = inject(HttpClient);

  currentIsUserLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  currentUserData: BehaviorSubject<JwtResponse | null> = new BehaviorSubject<JwtResponse | null>(null);

  constructor() {
    this.currentIsUserLoggedIn = new BehaviorSubject<boolean>(sessionStorage.getItem('token') != null);
    this.currentUserData = new BehaviorSubject<JwtResponse | null>(JSON.parse(sessionStorage.getItem('userData') || 'null'));
  }

  login(credentials: LoginRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(environment.apiUrl + 'auth/login', credentials).pipe(
      tap((userData) => {
        sessionStorage.setItem('userData', JSON.stringify(userData));
        this.currentUserData.next(userData);
        this.currentIsUserLoggedIn.next(true);
      }),
      catchError(this.handleError)
    );
  }

  // logout(): void {
  //   sessionStorage.removeItem('token');
  //   this.currentIsUserLoggedIn.next(false);
  // }

  private handleError(error: HttpErrorResponse) {
    let errorMsg = 'Algo salió mal; inténtalo de nuevo.';
    if (error.status === 400 && error.error) {
      errorMsg = error.error;
    }
    return throwError(() => new Error(errorMsg));
  }

  get userData(): Observable<JwtResponse | null> {
    return this.currentUserData.asObservable();
  }

  get userLoginOn(): Observable<boolean> {
    return this.currentIsUserLoggedIn.asObservable();
  }

  get userToken(): String {
    return this.currentUserData.getValue()?.token || '';
  }

}
