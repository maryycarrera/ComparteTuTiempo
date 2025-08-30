import { Component, inject } from '@angular/core';
import { Logout } from '../../auth/logout/logout';

@Component({
  selector: 'app-admin-profile',
  imports: [Logout],
  templateUrl: './admin-profile.html',
  styleUrl: './admin-profile.css'
})
export class AdminProfile {

}
