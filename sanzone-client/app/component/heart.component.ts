/**
 * Created by DEV on 12.03.2017.
 */
import {Component, Input} from '@angular/core';

@Component( {

    selector: 'heart',

    template: `<div>
                    <i class="glyphicon glyphicon-heart"
                       [style.color]="liked ? 'deeppink' : '#ccc'"
                       (click)="onClick()"></i>
                    <span>{{ likes }} </span>
               </div>`,

    styles: [
        `.glyphicon-heart {
            cursor: pointer;
        }`
    ]
} )

export class HeartComponent {

    @Input() likes = 0;
    @Input() liked = false;

    onClick() {

        this.liked = !this.liked;
        this.likes += this.liked ? 1 : -1;
    }
}