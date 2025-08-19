import { Component, inject, OnInit } from '@angular/core';
import { Navbar } from '../../shared/navbar/navbar';
import { LoginService } from '../../services/auth/login.service';
import { User } from '../../services/auth/user';
import { Subscription } from 'rxjs';
import { PersonalDetails } from '../../components/personal-details/personal-details';

@Component({
  selector: 'app-dashboard',
  imports: [Navbar, PersonalDetails],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  userLoginOn: boolean = false;
  userData?: User;

  private loginService = inject(LoginService);
  private userLoginSubscription: Subscription = new Subscription();

  ngOnInit(): void {
    this.userLoginSubscription.add(this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    }));
  }

  // ngOnDestroy(): void {
  //   this.userLoginSubscription.unsubscribe();
  // }
}
