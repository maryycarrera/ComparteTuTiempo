import { Component, inject } from '@angular/core';
import { SolidarityFundService } from '../../services/solidarity-fund/solidarity-fund.service';

@Component({
  selector: 'app-member-solidarity-fund',
  imports: [],
  templateUrl: './member-solidarity-fund.html',
  styleUrl: './member-solidarity-fund.css'
})
export class MemberSolidarityFund {

  errorMessage?: string;
  hours?: string;
  minutes?: string;

  private solidarityFundService = inject(SolidarityFundService);

  constructor() {
    this.solidarityFundService.getSolidarityFund().subscribe({
      next: (data) => {
        this.hours = data.object.hours;
        this.minutes = data.object.minutes;
      },
      error: (err) => {
        this.errorMessage = err && err.message ? err.message : String(err);
      },
      complete: () => {
        console.info('Datos del Fondo Solidario obtenidos.');
      }
    })
  }

}
