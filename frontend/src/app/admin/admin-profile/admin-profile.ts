import { Component, inject } from '@angular/core';
import { AdminService } from '../../services/admin/admin.service';
import { AdminDTO } from '../../services/admin/dto/admin-dto';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { ResourcesService } from '../../services/resources/resources.service';

@Component({
  selector: 'app-admin-profile',
  imports: [ReactiveFormsModule],
  templateUrl: './admin-profile.html',
  styleUrl: './admin-profile.css'
})
export class AdminProfile {

  currentAdmin?: AdminDTO;
  errorMessage?: string;
  editMode: boolean = false;

  private adminService = inject(AdminService);
  private resourcesService = inject(ResourcesService);
  private fb = inject(FormBuilder);

  profileForm = this.fb.group({
    username: [''],
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    lastName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    email: ['']
  });

  static readonly DEFAULT_PROFILE_PICTURE: string = environment.hostUrl + 'profilepics/black.png';

  profilePicture: string = AdminProfile.DEFAULT_PROFILE_PICTURE;

  constructor() {
    this.adminService.getProfile().subscribe({
      next: (adminData) => {
        this.currentAdmin = adminData;
        this.profileForm.patchValue({
          username: adminData.username,
          name: adminData.name,
          lastName: adminData.lastName,
          email: adminData.email
        });
        if (!this.editMode) {
          this.profileForm.disable();
        }
        let picUrl = this.profilePicture;
        if (adminData.profilePic && adminData.profilePic !== '') {
          picUrl = adminData.profilePic;
          if (!picUrl.startsWith('http')) {
            picUrl = environment.hostUrl + adminData.profilePic;
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
