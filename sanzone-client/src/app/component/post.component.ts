/**
 * Created by DEV on 13.03.2017.
 */
import { Component, Input } from '@angular/core';

@Component({

    selector: 'app-post',

    template: `<div class="row">
                   <div class="col-md-7">
                       <p class="bg-primary">{{title}}</p>
                       <em>{{article}}</em> 
                   </div>
                   <div class="col-md-5">
                       <a href="#">
                           <img class="img-responsive center-block" src={{image}} alt="">
                       </a>
                   </div>
               </div>
               <hr>`,

    styles: ['']
})

export class PostComponent {

    @Input() id = 0;
    @Input() image = "";
    @Input() title = "";
    @Input() article = "";

}