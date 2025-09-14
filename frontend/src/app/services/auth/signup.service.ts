import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { SignupRequest } from '../../payload/request/signup-request';
import { Observable, throwError } from 'rxjs';
import { MessageResponse } from '../../payload/response/message-response';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { UserCreationService } from '../user-creation-service';

@Injectable({
  providedIn: 'root'
})
export class SignupService implements UserCreationService {

  private http = inject(HttpClient);

  create(request: SignupRequest): Observable<MessageResponse> {
    return this.signup(request);
  }

  private signup(data: SignupRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(environment.apiUrl + 'auth/signup', data).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMsg = 'Error al registrar usuario.';
    if (error.error && error.error.message) {
      errorMsg = error.error.message;
    } else if (error.error) {
      errorMsg = error.error;
    }
    return throwError(() => new Error(errorMsg));
  }
}
