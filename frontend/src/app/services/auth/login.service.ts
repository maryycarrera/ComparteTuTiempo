import { inject, Injectable } from '@angular/core';
import { LoginRequest } from './login-request';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError, BehaviorSubject, tap } from 'rxjs';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private http = inject(HttpClient);

  currentUserLoginOn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  currentUserData: BehaviorSubject<User> = new BehaviorSubject<User>({
    id: 0,
    username: '',
    name: '',
    lastName: '',
    email: ''
  });

  constructor() { }

  login(credentials: LoginRequest): Observable<User> {
    return this.http.get<User>('/assets/data.json').pipe(
      tap((userData: User) => {
        this.currentUserData.next(userData);
        this.currentUserLoginOn.next(true);
      }),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
    } else {
      console.error('Backend returned code:', error.status, 'body was:', error.error);
    }
    return throwError(() => new Error('Something went wrong; please try again.'));
  }

  get userData(): Observable<User> {
    return this.currentUserData.asObservable();
  }

  get userLoginOn(): Observable<boolean> {
    return this.currentUserLoginOn.asObservable();
  }

}
