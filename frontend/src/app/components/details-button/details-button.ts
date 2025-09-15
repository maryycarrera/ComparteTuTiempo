import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-details-button',
  imports: [],
  templateUrl: './details-button.html',
  styleUrl: './details-button.css'
})
export class DetailsButton {

  @Output() clickDetails = new EventEmitter<void>();

  onClick() {
    this.clickDetails.emit();
  }

}
