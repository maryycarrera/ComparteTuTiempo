import { Component, EventEmitter, Output } from '@angular/core';
import { BaseIconButton } from '../base-icon-button/base-icon-button';

@Component({
  selector: 'app-details-button',
  imports: [BaseIconButton],
  templateUrl: './details-button.html',
  styleUrl: './details-button.css'
})
export class DetailsButton {

  @Output() clickDetails = new EventEmitter<void>();

  onClick() {
    this.clickDetails.emit();
  }

}
