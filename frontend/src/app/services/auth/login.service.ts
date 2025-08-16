import { inject, Injectable } from '@angular/core';
import { LoginRequest } from './login-request';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private http = inject(HttpClient);

  constructor() { }

  login(credentials:LoginRequest):Observable<User> {
    return this.http.get<User>('/assets/data.json').pipe(
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

}
