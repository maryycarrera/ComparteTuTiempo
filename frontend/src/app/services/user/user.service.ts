import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { User } from '../auth/user';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private http = inject(HttpClient);

  constructor() { }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(environment.apiUrl + `users/${id}`).pipe(
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
