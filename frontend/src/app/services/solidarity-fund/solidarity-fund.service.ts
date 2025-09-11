import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LoginService } from '../auth/login.service';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MessageResponse } from '../auth/payload/response/message-response';

@Injectable({
  providedIn: 'root'
})
export class SolidarityFundService {

  private http = inject(HttpClient);
  private loginService = inject(LoginService);

  getSolidarityFund(): Observable<MessageResponse> {
    const token = this.loginService.userToken;
    return this.http.get<MessageResponse>(environment.apiUrl + 'solidarity-fund', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMsg = 'Error al obtener Fondo Solidario.';
    if (error.error && error.error.message) {
      errorMsg = error.error.message;
    } else if (error.error) {
      errorMsg = error.error;
    }
    return throwError(() => new Error(errorMsg));
  }

}
