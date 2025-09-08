import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AdminDTO } from './admin-dto';
import { environment } from '../../../environments/environment';
import { LoginService } from '../auth/login.service';
import { SignupRequest } from '../auth/payload/request/signup-request';
import { MessageResponse } from '../auth/payload/response/message-response';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private http = inject(HttpClient);
  private loginService = inject(LoginService);
  private adminUrl = environment.apiUrl + 'admins';

  getProfile(): Observable<AdminDTO> {
    const token = this.loginService.userToken;
    return this.http.get<AdminDTO>(environment.apiUrl + 'auth/profile', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.handleError(error, 'Error al obtener perfil de usuario.'))
    );
  }

  create(data: SignupRequest): Observable<MessageResponse> {
    const token = this.loginService.userToken;
    return this.http.post<MessageResponse>(this.adminUrl, data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.handleError(error, 'Error al crear administrador.'))
    );
  }

  private handleError(error: HttpErrorResponse, defaultMessage: string) {
    let errorMsg = defaultMessage;
    if (error.error && error.error.message) {
      errorMsg = error.error.message;
    } else if (error.error) {
      errorMsg = error.error;
    }
    return throwError(() => new Error(errorMsg));
  }

}
