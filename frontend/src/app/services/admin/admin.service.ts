import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AdminDTO } from './dto/admin-dto';
import { environment } from '../../../environments/environment';
import { LoginService } from '../auth/login.service';
import { SignupRequest } from '../../payload/request/signup-request';
import { MessageResponse } from '../../payload/response/message-response';
import { UserCreationService } from '../user-creation-service';
import { ListMessageResponse } from '../../payload/response/list-message-response';
import { AdminForListDTO } from './dto/admin-for-list-dto';
import { ErrorHandler } from '../error-handler';

@Injectable({
  providedIn: 'root'
})
export class AdminService implements UserCreationService {

  private http = inject(HttpClient);
  private loginService = inject(LoginService);
  private errorHandler = new ErrorHandler();
  private adminUrl = environment.apiUrl + 'admins';

  getProfile(): Observable<AdminDTO> {
    const token = this.loginService.userToken;
    return this.http.get<AdminDTO>(environment.apiUrl + 'auth/profile', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.errorHandler.handleError(error, 'Error al obtener perfil de usuario.'))
    );
  }

  create(data: SignupRequest): Observable<MessageResponse> {
    const token = this.loginService.userToken;
    return this.http.post<MessageResponse>(this.adminUrl, data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.errorHandler.handleError(error, 'Error al crear administrador.'))
    );
  }

  getAllAdmins(): Observable<ListMessageResponse<AdminForListDTO>> {
    const token = this.loginService.userToken;
    return this.http.get<ListMessageResponse<AdminForListDTO>>(this.adminUrl, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(error => this.errorHandler.handleError(error, 'Error al obtener lista de administradores.'))
    );
  }

  deleteAdmin(adminId: string): Observable<string> {
    const token = this.loginService.userToken;
    return this.http.delete<string>(`${this.adminUrl}/${adminId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      responseType: 'text' as 'json'
    }).pipe(
      catchError(error => this.errorHandler.handleError(error, 'Error al eliminar administrador.'))
    );
  }

}
