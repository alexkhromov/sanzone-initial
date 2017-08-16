import { Component } from '@angular/core';

@Component({
    selector: 'my-dashboard',
    templateUrl: '<h3>Top Heroes</h3>
    <div class="grid grid-pad">
    <div *ngFor="let hero of heroes" class="col-1-4">
    <div class="module hero">
        <h4>{{hero.name}}</h4>
</div>
</div>
</div>',
})
export class DashboardComponent { }