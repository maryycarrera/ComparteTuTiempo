import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LoginService } from '../auth/login.service';
import { MemberProfileDTO } from './dto/member-profile-dto';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ListMessageResponse } from '../../payload/response/list-message-response';
import { MemberListDTO } from './dto/member-list-dto';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  private http = inject(HttpClient);
  private loginService = inject(LoginService);

  getAllMembers(): Observable<ListMessageResponse<MemberListDTO>> {
    const token = this.loginService.userToken;
    return this.http.get<ListMessageResponse<MemberListDTO>>(environment.apiUrl + 'members', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.handleError(error, 'Error al obtener lista de miembros.'))
    );
  }

  getProfile(): Observable<MemberProfileDTO> {
    const token = this.loginService.userToken;
    return this.http.get<MemberProfileDTO>(environment.apiUrl + 'auth/profile', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.handleError(error, 'Error al obtener perfil de usuario.'))
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
