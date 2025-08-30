import { Component, inject, OnInit } from '@angular/core';
import { AdminProfile } from '../../../admin/admin-profile/admin-profile';
import { LoginService } from '../../../services/auth/login.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-profile',
  imports: [AdminProfile],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {

  userIsAdmin: boolean = false;

  private loginService = inject(LoginService);
  private subscription: Subscription = new Subscription();

  ngOnInit() {
    this.subscription.add(this.loginService.userIsAdmin.subscribe({
      next: (userAdminOn) => {
        this.userIsAdmin = userAdminOn;
      }
    }));
  }

}
