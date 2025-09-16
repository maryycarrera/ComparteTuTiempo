import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LoginService } from '../auth/login.service';
import { MemberProfileDTO } from './dto/member-profile-dto';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ListMessageResponse } from '../../payload/response/list-message-response';
import { MemberListDTO } from './dto/member-list-dto';
import { MemberListForAdminDTO } from './dto/member-list-for-admin-dto';
import { MemberDTO } from './dto/member-dto';
import { MessageResponse } from '../../payload/response/message-response';
import { MemberForMemberDTO } from './dto/member-for-member-dto';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  private http = inject(HttpClient);
  private loginService = inject(LoginService);

  getAllMembers(): Observable<ListMessageResponse<MemberListDTO|MemberListForAdminDTO>> {
    const token = this.loginService.userToken;
    return this.http.get<ListMessageResponse<MemberListDTO|MemberListForAdminDTO>>(environment.apiUrl + 'members', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.handleError(error, 'Error al obtener lista de miembros.'))
    );
  }

  getMemberById(id: string): Observable<MessageResponse<MemberDTO|MemberForMemberDTO>> {
    const token = this.loginService.userToken;
    return this.http.get<MessageResponse<MemberDTO|MemberForMemberDTO>>(environment.apiUrl + `members/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.handleError(error, 'Error al obtener miembro por ID.'))
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

  deleteMember(memberId: string): Observable<String> {
    const token = this.loginService.userToken;
    return this.http.delete<String>(environment.apiUrl + `members/${memberId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      responseType: 'text' as 'json'
    }).pipe(
      catchError(error => this.handleError(error, 'Error al eliminar miembro.'))
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
