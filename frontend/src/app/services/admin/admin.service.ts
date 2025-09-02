import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AdminDTO } from './admin-dto';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private http = inject(HttpClient);

  getProfile(): Observable<AdminDTO> {
    return this.http.get<AdminDTO>(environment.apiUrl + 'auth/profile').pipe(
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
