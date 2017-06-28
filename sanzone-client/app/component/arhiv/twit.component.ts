/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';
import {PostService} from "../../service/post.service";

@Component( {

    selector: 'twit',

    template: `<ul>
                   <post *ngFor = "let post of posts"
                         [image]="post.image"
                         [title]="post.title"
                         
                         [article]="post.article">
                         <!--[likes]="post.likes"-->
                         <!--[liked]="post.liked"--></post>
               </ul>`
} )

export class TwitComponent {

    posts;

    constructor( postService : PostService ) {

        this.posts = postService.getPosts();
    }
}