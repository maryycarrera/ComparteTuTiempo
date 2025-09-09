import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin/admin.service';
import { SignupRequest } from '../../services/auth/payload/request/signup-request';

@Component({
  selector: 'app-create-admin',
  imports: [ReactiveFormsModule],
  templateUrl: './create-admin.html',
  styleUrl: './create-admin.css'
})
export class CreateAdmin {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private adminService = inject(AdminService);

  createError: string = '';
  createSuccess: string = '';
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  createForm = this.fb.group({
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

  private passwordsMatchValidator(formGroup: any) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordsMismatch: true };
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  get name() { return this.createForm.controls.name; }
  get lastName() { return this.createForm.controls.lastName; }
  get username() { return this.createForm.controls.username; }
  get email() { return this.createForm.controls.email; }
  get password() { return this.createForm.controls.password; }
  get confirmPassword() { return this.createForm.controls.confirmPassword; }

  create() {
      if (this.createForm.valid) {
        const signupRequest: SignupRequest = {
          name: this.name.value!,
          lastName: this.lastName.value!,
          username: this.username.value!,
          email: this.email.value!,
          password: this.password.value!
        };

        this.adminService.create(signupRequest).subscribe({
          next: () => {
            this.createSuccess = 'Cuenta creada correctamente.';
            this.router.navigateByUrl('/administradores', { state: { createSuccess: this.createSuccess } });
            this.createForm.reset();
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
            this.createError = msg;
          }
        });
      } else {
        this.createForm.markAllAsTouched();
        this.createError = 'Ha ocurrido un error. Por favor, inténtalo de nuevo.';
      }
    }

}
