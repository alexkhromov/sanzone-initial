/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from "angular2/core";
import {PostComponent} from "./post.component";
import {PostService} from "../service/post.service";
@Component({

    selector: 'twitt',

    template: `<ul>
                   <post *ngFor = "#post of posts"
                         [image]="post.image"
                         [author]="post.author"
                         [twitt-handle]="post.twittHandle"
                         [message]="post.message"
                         [likes]="post.likes"
                         [liked]="post.liked"></post>
               </ul>`,

    directives: [ PostComponent ],
    providers: [ PostService ]
})

export class TwittComponent {

    posts;

    constructor( postService : PostService ) {

        this.posts = postService.getPosts();
    }
}