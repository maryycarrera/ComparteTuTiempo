import { Component, inject } from '@angular/core';
import { User } from '../../services/auth/user';
import { UserService } from '../../services/user/user.service';
import { environment } from '../../../environments/environment';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginService } from '../../services/auth/login.service';

@Component({
  selector: 'app-personal-details',
  imports: [ReactiveFormsModule],
  templateUrl: './personal-details.html',
  styleUrl: './personal-details.css'
})
export class PersonalDetails {

  errorMessage: string = '';
  user?: User;
  userLoginOn: boolean = false;
  editMode: boolean = false;

  private userService = inject(UserService);
  private formBuilder = inject(FormBuilder);
  private loginService = inject(LoginService);

  registerForm = this.formBuilder.group({
    id: [''],
    name: ['', Validators.required],
    lastName: ['', Validators.required]
  });

  // constructor() {
  //   this.userService.getUser(environment.userId).subscribe({
  //     next: (userData) => {
  //       this.user = userData;
  //       this.registerForm.controls.id.setValue(userData.id.toString());
  //       this.registerForm.controls.name.setValue(userData.name);
  //       this.registerForm.controls.lastName.setValue(userData.lastName);
  //     },
  //     error: (err) => this.errorMessage = err,
  //     complete: () => console.info('User data fetched successfully')
  //   });

  //   this.loginService.userLoginOn.subscribe({
  //     next: (isLoggedIn) => {
  //       this.userLoginOn = isLoggedIn;
  //     }
  //   });
  // }

  get name() {
    return this.registerForm.controls.name;
  }

  get lastName() {
    return this.registerForm.controls.lastName;
  }

  savePersonalDetailsData() {
    if (this.registerForm.valid) {
      this.userService.updateUser(this.registerForm.value as unknown as User).subscribe({
        next: () => {
          this.editMode = false;
          this.user = this.registerForm.value as unknown as User;
        },
        error: (err: any) => console.error(err)
      });
    }
  }

}
