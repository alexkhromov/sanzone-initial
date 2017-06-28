/**
 * Created by DEV on 13.03.2017.
 */
import {Component, Input} from '@angular/core';

@Component( {

    selector: 'post',

    template: `<div class="media pad">
                   <div class="media-left">
                       <a href="#">
                         <img class="media-object img-rounded" src={{image}}>
                       </a>
                   </div>
                   <div class="media-body">
                       <h4 class="media-heading twit-head">{{title}}</h4>
                       <!--<h4 class="twit-handle">&nbsp;{{twitHandle}}</h4>
                       <h5>{{message}}</h5>
                       <!--<heart [likes]="likes" [liked]="liked"></heart>-->
                   </div>
               </div>`,

    styles: [

        `.pad {
            padding: 10;
        }
        
        .twit-head, .twit-handle {
            font-weight: bold;
            display: inline;
        }
        
        .twit-handle {
            color: #ccc;
        }`
    ]
} )

export class PostComponent {

    @Input() image = "";
    @Input() title = "";
    @Input( 'twit-handle' ) twitHandle = "";
    @Input() message = "";
    @Input() likes = 0;
    @Input() liked = false;
}