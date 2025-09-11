import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { LoginService } from '../../../services/auth/login.service';
import { Subscription } from 'rxjs';
import { MemberSolidarityFund } from '../../../member/member-solidarity-fund/member-solidarity-fund';
import { AdminSolidarityFund } from '../../../admin/admin-solidarity-fund/admin-solidarity-fund';

@Component({
  selector: 'app-solidarity-fund',
  imports: [MemberSolidarityFund, AdminSolidarityFund],
  templateUrl: './solidarity-fund.html',
  styleUrl: './solidarity-fund.css'
})
export class SolidarityFund implements OnInit, OnDestroy {

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

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
