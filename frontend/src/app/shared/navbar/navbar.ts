import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { LoginService } from '../../services/auth/login.service';
import { LoggedoutNavbar } from '../loggedout-navbar/loggedout-navbar';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule, LoggedoutNavbar],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar implements OnInit {

  userLoginOn: boolean = false;

  private loginService = inject(LoginService);
  private router = inject(Router);

  private subscription: Subscription = new Subscription();

  ngOnInit(): void {
    this.subscription.add(this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    }));
  }
}
