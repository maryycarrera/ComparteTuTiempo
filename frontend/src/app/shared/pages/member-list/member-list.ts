import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { MemberService } from '../../../services/member/member.service';
import { MemberListDTO } from '../../../services/member/dto/member-list-dto';
import { Subscription } from 'rxjs';
import { MemberListForAdminDTO } from '../../../services/member/dto/member-list-for-admin-dto';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BaseIconButton } from '../../../components/base-icon-button/base-icon-button';

@Component({
  selector: 'app-member-list',
  imports: [BaseIconButton, FormsModule],
  templateUrl: './member-list.html',
  styleUrl: './member-list.css'
})
export class MemberList implements OnInit, OnDestroy {

  private memberService = inject(MemberService);
  private router = inject(Router);
  private subscription: Subscription = new Subscription();

  errorMessage?: string;
  members?: (MemberListDTO|MemberListForAdminDTO)[];
  searchText: string = '';
  filteredMembers?: (MemberListDTO|MemberListForAdminDTO)[];

  ngOnInit(): void {
    this.subscription.add(
      this.memberService.getAllMembers().subscribe({
        next: (response) => {
          this.members = response.objects;
          this.filteredMembers = this.members;
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

  viewMemberDetails(memberId: string) {
    this.router.navigate(['/miembros', memberId]);
  }

  deleteMember(memberId: string) {
    this.memberService.deleteMember(memberId).subscribe({
      next: () => {
        this.members = this.members?.filter(m => m.id !== memberId);
        this.filterMembers();
      },
      error: (error) => {
        this.errorMessage = error && error.message ? error.message : String(error);
      }
    })
  }

  // START Generado con GitHub Copilot Chat Extension
  filterMembers() {
    if (!this.searchText?.trim() || !this.members) {
      this.filteredMembers = this.members;
      return;
    }
    const search = this.searchText.trim().toLowerCase();
    this.filteredMembers = this.members.filter(member =>
      member.username.toLowerCase().includes(search)
    );
  }

  onSearchSubmit(event: Event) {
    event.preventDefault();
    this.filterMembers();
  }
  // END Generado con GitHub Copilot Chat Extension

}
