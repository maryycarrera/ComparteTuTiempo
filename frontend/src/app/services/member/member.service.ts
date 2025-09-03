import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LoginService } from '../auth/login.service';
import { MemberProfileDTO } from './member-profile-dto';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  private http = inject(HttpClient);
  private loginService = inject(LoginService);

  getProfile(): Observable<MemberProfileDTO> {
    const token = this.loginService.userToken;
    return this.http.get<MemberProfileDTO>(environment.apiUrl + 'auth/profile', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMsg = 'Error al obtener perfil de usuario.';
    if (error.error && error.error.message) {
      errorMsg = error.error.message;
    } else if (error.error) {
      errorMsg = error.error;
    }
    return throwError(() => new Error(errorMsg));
  }

}
