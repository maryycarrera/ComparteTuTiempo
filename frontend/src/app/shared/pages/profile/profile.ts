import { Component, inject, OnInit } from '@angular/core';
import { AdminProfile } from '../../../admin/admin-profile/admin-profile';
import { MemberProfile } from '../../../member/member-profile/member-profile';
import { Logout } from '../../../auth/logout/logout';
import { LoginService } from '../../../services/auth/login.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-profile',
  imports: [AdminProfile, MemberProfile, Logout],
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
