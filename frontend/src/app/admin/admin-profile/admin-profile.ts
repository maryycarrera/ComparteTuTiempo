import { Component, inject } from '@angular/core';
import { LogoutService } from '../../services/auth/logout.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-profile',
  imports: [],
  templateUrl: './admin-profile.html',
  styleUrl: './admin-profile.css'
})
export class AdminProfile {

  private logoutService = inject(LogoutService);
  private router = inject(Router);

  logout() {
    this.logoutService.logout().subscribe({
      complete: () => {
        this.router.navigateByUrl('/');
      }
    });
  }

}
