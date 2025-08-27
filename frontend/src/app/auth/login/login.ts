import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../services/auth/login.service';
import { LoginRequest } from '../../services/auth/payload/request/login-request';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private loginService = inject(LoginService);

  loginError: string = '';
  loginSuccess: string = '';

  loginForm = this.fb.group({
      username: ['', [
        Validators.required,
        Validators.pattern('^[a-z0-9_.]+$'),
        Validators.minLength(5),
        Validators.maxLength(15)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(12)
      ]]
    });

  constructor() { }

  get username() {
    return this.loginForm.controls.username;
  }

  get password() {
    return this.loginForm.controls.password;
  }

  login() {
    if (this.loginForm.valid) {
      this.loginService.login(this.loginForm.value as LoginRequest).subscribe({
        next: (userData) => {
          this.loginSuccess = 'Inicio de sesión exitoso.';
        },
        error: (errorData) => {
          this.loginError = errorData;
        },
        complete: () => {
          this.router.navigateByUrl('/inicio');
          this.loginForm.reset();
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
      this.loginError = 'Credenciales no válidas.';
    }
  }

}
