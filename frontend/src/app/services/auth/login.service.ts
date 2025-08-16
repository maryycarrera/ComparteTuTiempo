import { inject, Injectable } from '@angular/core';
import { LoginRequest } from './login-request';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private http = inject(HttpClient);

  constructor() { }

  login(credentials:LoginRequest):Observable<User> {
    return this.http.get<User>('/assets/data.json');
  }

}
