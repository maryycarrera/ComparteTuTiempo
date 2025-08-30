import { Component } from '@angular/core';
import { Logout } from '../../auth/logout/logout';

@Component({
  selector: 'app-member-profile',
  imports: [Logout],
  templateUrl: './member-profile.html',
  styleUrl: './member-profile.css'
})
export class MemberProfile {

}
