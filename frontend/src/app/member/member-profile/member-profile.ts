import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MemberProfileDTO } from '../../services/member/member-profile-dto';
import { MemberService } from '../../services/member/member.service';
import { ResourcesService } from '../../services/resources/resources.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-member-profile',
  imports: [ReactiveFormsModule],
  templateUrl: './member-profile.html',
  styleUrl: './member-profile.css'
})
export class MemberProfile {

  currentMember?: MemberProfileDTO;
  errorMessage?: string;
  editMode: boolean = false;

  private memberService = inject(MemberService);
  private resourcesService = inject(ResourcesService);
  private fb = inject(FormBuilder);

  profileForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    lastName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    biography: ['', [Validators.maxLength(500)]]
  });

  static readonly DEFAULT_PROFILE_PICTURE: string = environment.hostUrl + 'profilepics/black.png';

  email: string = '';
  profilePicture: string = MemberProfile.DEFAULT_PROFILE_PICTURE;
  dateOfMembership: string = '';
  hours: string = '';
  minutes: string = '';

  constructor() {
    this.memberService.getProfile().subscribe({
      next: (memberData) => {
        this.currentMember = memberData;
        this.profileForm.patchValue({
          name: memberData.name,
          lastName: memberData.lastName,
          biography: memberData.biography
        });
        this.email = memberData.email;
        this.dateOfMembership = memberData.dateOfMembership;
        this.hours = memberData.hours;
        this.minutes = memberData.minutes;

        if (!this.editMode) {
          this.profileForm.disable();
        }
        let picUrl = this.profilePicture;
        if (memberData.profilePic && memberData.profilePic !== '') {
          picUrl = memberData.profilePic;
          if (!picUrl.startsWith('http')) {
            picUrl = environment.hostUrl + memberData.profilePic;
          }
        }
        this.resourcesService.getProfilePicture(picUrl).subscribe({
          next: (blob) => {
            this.profilePicture = URL.createObjectURL(blob);
          },
          error: (err) => {
            this.errorMessage = err && err.message ? err.message : String(err);
          }
        });
      },
      error: (err) => {
        this.errorMessage = err && err.message ? err.message : String(err);
      },
      complete: () => {
        console.info('Datos del perfil cargados correctamente');
      },
    })
  }

}
