import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin/admin.service';
import { ResourcesService } from '../../services/resources/resources.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AdminDTO } from '../../services/admin/dto/admin-dto';

@Component({
  selector: 'app-admin-info',
  imports: [],
  templateUrl: './admin-info.html',
  styleUrl: './admin-info.css'
})
export class AdminInfo implements OnInit, OnDestroy {

  private adminService = inject(AdminService);
  private resourcesService = inject(ResourcesService);
  private activatedRoute = inject(ActivatedRoute);
  private subscription: Subscription = new Subscription();
  
  static readonly DEFAULT_PROFILE_PICTURE: string = environment.hostUrl + 'profilepics/black.png';

  adminId!: string;
  admin?: AdminDTO;
  fullName?: string;
  errorMessage?: string;
  profilePicture: string = AdminInfo.DEFAULT_PROFILE_PICTURE;

  ngOnInit(): void {
    this.subscription.add(
      this.activatedRoute.params.subscribe(params => {
        this.adminId = params['id'];
        this.adminService.getAdminById(this.adminId).subscribe({
          next: (response) => {
            this.admin = response.object;
            this.fullName = this.admin?.name + ' ' + this.admin?.lastName;

            let picUrl = this.profilePicture;
            if (response.object?.profilePic && response.object.profilePic !== '') {
              picUrl = response.object.profilePic;
              if (!picUrl.startsWith('http')) {
                picUrl = environment.hostUrl + response.object.profilePic;
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
          error: (error) => {
            this.errorMessage = error && error.message ? error.message : String(error);
          }
        });
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
