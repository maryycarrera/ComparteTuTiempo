import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { MemberService } from '../../../services/member/member.service';
import { MemberListDTO } from '../../../services/member/dto/member-list-dto';
import { Subscription } from 'rxjs';
import { MemberListForAdminDTO } from '../../../services/member/dto/member-list-for-admin-dto';

@Component({
  selector: 'app-member-list',
  imports: [],
  templateUrl: './member-list.html',
  styleUrl: './member-list.css'
})
export class MemberList implements OnInit, OnDestroy {

  private memberService = inject(MemberService);
  private subscription: Subscription = new Subscription();

  errorMessage?: string;
  members?: (MemberListDTO|MemberListForAdminDTO)[];

  ngOnInit(): void {
    this.subscription.add(
      this.memberService.getAllMembers().subscribe({
        next: (response) => {
          this.members = response.objects;
          if (!this.members || this.members.length === 0) {
            this.errorMessage = response.message;
          }
        },
        error: (error) => {
          this.errorMessage = error && error.message ? error.message : String(error);
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
