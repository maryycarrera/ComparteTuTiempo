import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Navbar } from '../../shared/navbar/navbar';
import { LoginService } from '../../services/auth/login.service';
import { User } from '../../services/auth/user';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  imports: [Navbar],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit, OnDestroy {

  userLoginOn: boolean = false;
  userData?: User;

  private loginService = inject(LoginService);
  private userLoginSubscription: Subscription = new Subscription();
  private userDataSubscription: Subscription = new Subscription();

  ngOnInit(): void {
    this.userLoginSubscription.add(this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    }));

    this.userDataSubscription.add(this.loginService.userData.subscribe({
      next: (userData) => {
        this.userData = userData;
      }
    }));
  }

  ngOnDestroy(): void {
    this.userLoginSubscription.unsubscribe();
    this.userDataSubscription.unsubscribe();
  }
}
