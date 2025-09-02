import { Component, inject } from '@angular/core';
import { AdminService } from '../../services/admin/admin.service';
import { AdminDTO } from '../../services/admin/admin-dto';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-admin-profile',
  imports: [],
  templateUrl: './admin-profile.html',
  styleUrl: './admin-profile.css'
})
export class AdminProfile {

  currentAdmin?: AdminDTO;
  errorMessage?: string;
  editMode: boolean = false;

  private adminService = inject(AdminService);
  private fb = inject(FormBuilder);

  profileForm = this.fb.group({
    username: [''],
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    lastName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]]
  });

  profilePicture?: string;

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
        this.profilePicture = adminData.profilePic;
      },
      error: (err) => {
        this.errorMessage = err;
      },
      complete: () => {
        console.info('Datos del perfil cargados correctamente');
      },
    })
  }

}
