import { Component, inject } from '@angular/core';
import { User } from '../../services/auth/user';
import { UserService } from '../../services/user/user.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-personal-details',
  imports: [],
  templateUrl: './personal-details.html',
  styleUrl: './personal-details.css'
})
export class PersonalDetails {

  errorMessage: String = '';
  user?: User;

  private userService = inject(UserService);

  constructor() {
    this.userService.getUser(environment.userId).subscribe({
      next: (userData) => this.user = userData,
      error: (err) => this.errorMessage = err,
      complete: () => console.info('User data fetched successfully')
    });
  }

}
