import { Component, inject } from '@angular/core';
import { AdminService } from '../../services/admin/admin.service';
import { AdminDTO } from '../../services/admin/dto/admin-dto';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { ResourcesService } from '../../services/resources/resources.service';
import { Logout } from '../../auth/logout/logout';
import { AdminEditDTO } from '../../services/admin/dto/admin-edit-dto';

@Component({
  selector: 'app-admin-profile',
  imports: [ReactiveFormsModule, Logout],
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
        this.profileForm.disable();
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

  edit() {
    this.editMode = true;
    this.profileForm.get('name')?.enable();
    this.profileForm.get('lastName')?.enable();
    this.profileForm.get('username')?.disable();
    this.profileForm.get('email')?.disable();
  }

  cancel() {
    this.editMode = false;
    this.profileForm.patchValue({
      username: this.currentAdmin?.username,
      name: this.currentAdmin?.name,
      lastName: this.currentAdmin?.lastName,
      email: this.currentAdmin?.email
    });
    this.profileForm.disable();
    this.profileForm.markAsUntouched();
    this.errorMessage = undefined;
  }

  save() {
    if (this.profileForm.valid && this.currentAdmin) {
      const updatedAdmin: AdminEditDTO = {
        name: this.profileForm.get('name')?.value!,
        lastName: this.profileForm.get('lastName')?.value!
      }
      this.adminService.editProfile(updatedAdmin).subscribe({
        next: (response) => {
          this.currentAdmin = response.object;
          this.editMode = false;
          this.profileForm.disable();
          this.profileForm.markAsUntouched();
          this.errorMessage = undefined;
        }
      })
    }
  }

}
