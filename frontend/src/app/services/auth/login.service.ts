import { inject, Injectable } from '@angular/core';
import { LoginRequest } from './login-request';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError, BehaviorSubject, tap, map } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private http = inject(HttpClient);

  currentUserLoginOn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  currentUserData: BehaviorSubject<String> = new BehaviorSubject<String>('');

  constructor() {
    this.currentUserLoginOn = new BehaviorSubject<boolean>(sessionStorage.getItem('token') != null);
    this.currentUserData = new BehaviorSubject<String>(sessionStorage.getItem('token') || '');
  }

  login(credentials: LoginRequest): Observable<any> {
    return this.http.post<any>(environment.hostUrl + 'auth/login', credentials).pipe(
      tap((userData) => {
        sessionStorage.setItem('token', userData.token);
        this.currentUserData.next(userData.token);
        this.currentUserLoginOn.next(true);
      }),
      map((userData) => userData.token),
      catchError(this.handleError)
    );
  }

  logout(): void {
    sessionStorage.removeItem('token');
    this.currentUserLoginOn.next(false);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
    } else {
      console.error('Backend returned error:', error);
    }
    return throwError(() => new Error('Something went wrong; please try again.'));
  }

  get userData(): Observable<String> {
    return this.currentUserData.asObservable();
  }

  get userLoginOn(): Observable<boolean> {
    return this.currentUserLoginOn.asObservable();
  }

  get userToken(): String {
    return this.currentUserData.getValue();
  }

}
