/**
 * Created by DEV on 13.03.2017.
 */
import {Component, Input} from "angular2/core";
import {HeartComponent} from "./heart.component";

@Component({

    selector: 'post',

    template: `<div class="media pad">
                   <div class="media-left">
                       <a href="#">
                         <img class="media-object img-rounded" src={{image}}>
                       </a>
                   </div>
                   <div class="media-body">
                       <h4 class="media-heading twitt-head">{{author}}</h4>
                       <h4 class="twitt-handle">&nbsp;{{twittHandle}}</h4>
                       <h5>{{message}}</h5>
                       <heart [likes]="likes" [liked]="liked"></heart>
                   </div>
               </div>`,

    directives: [ HeartComponent ],

    styles: [

        `.pad {
            padding: 10;
        }
        
        .twitt-head, .twitt-handle {
            font-weight: bold;
            display: inline;
        }
        
        .twitt-handle {
            color: #ccc;
        }`
    ]
})

export class PostComponent {

    @Input() image = "";
    @Input() author = "";
    @Input( 'twitt-handle' ) twittHandle = "";
    @Input() message = "";
    @Input() likes = 0;
    @Input() liked = false;
}