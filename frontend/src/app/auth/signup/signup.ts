import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SignupRequest } from '../../services/auth/payload/request/signup-request';
import { SignupService } from '../../services/auth/signup.service';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule],
  templateUrl: './signup.html',
  styleUrl: './signup.css'
})
export class Signup {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private signupService = inject(SignupService);

  signupError: string = '';
  signupSuccess: string = '';
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  signupForm = this.fb.group({
    name: ['', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100)
    ]],
    lastName: ['', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100)
    ]],
    username: ['', [
      Validators.required,
      Validators.pattern('^[a-z0-9_.]+$'),
      Validators.minLength(5),
      Validators.maxLength(15)
    ]],
    email: ['', [
      Validators.required,
      Validators.email,
      Validators.maxLength(255)
    ]],
    password: ['', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(12)
    ]],
    confirmPassword: ['', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(12)
    ]]
  }, { validators: this.passwordsMatchValidator });

  // START Generado con GitHub Copilot Chat Extension
  private passwordsMatchValidator(formGroup: any) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordsMismatch: true };
  }
  // END Generado con GitHub Copilot Chat Extension

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  get name() { return this.signupForm.controls.name; }
  get lastName() { return this.signupForm.controls.lastName; }
  get username() { return this.signupForm.controls.username; }
  get email() { return this.signupForm.controls.email; }
  get password() { return this.signupForm.controls.password; }
  get confirmPassword() { return this.signupForm.controls.confirmPassword; }

  signup() {
    if (this.signupForm.valid) {
      const signupRequest: SignupRequest = {
        name: this.name.value!,
        lastName: this.lastName.value!,
        username: this.username.value!,
        email: this.email.value!,
        password: this.password.value!
      };

      this.signupService.create(signupRequest).subscribe({
        next: () => {
          this.signupSuccess = 'Cuenta creada correctamente. Inicia sesión para comenzar a utilizar el Banco de Tiempo.';
          this.router.navigateByUrl('/iniciar-sesion', { state: { successMsg: this.signupSuccess } });
          this.signupForm.reset();
        },
        error: (error) => {
          let msg = '';
          if (typeof error === 'string') {
            msg = error.replace('Error: ', '');
          } else if (error && typeof error.message === 'string') {
            msg = error.message.replace('Error: ', '');
          } else {
            msg = 'Error al crear la cuenta. Por favor, inténtalo de nuevo.';
          }
          this.signupError = msg;
        }
      });
    } else {
      this.signupForm.markAllAsTouched();
      this.signupError = 'Ha ocurrido un error. Por favor, inténtalo de nuevo.';
    }
  }

}
