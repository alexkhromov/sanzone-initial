/**
 * Created by DEV on 13.03.2017.
 */
import {Component} from '@angular/core';
import {PostService} from "../service/post.service";

@Component( {

    selector: 'twit',

    template: `<div class="container">
                   <ul>
                       <post *ngFor = "let post of posts"
                         [image]="post.image"
                         [title]="post.title"
                         [article]="post.article">
                       </post>
                   </ul>
               
               <!-- FOOTER -->
                   <footer>
                      <p class="pull-right"><a href="#">Вернуться к началу</a></p>
                      <p>&copy; 2017 EMFs. &middot; <a href="#">Конфиденциально</a>
                   </footer>
               </div>`
} )

export class TwitComponent {

    posts;

    constructor( postService : PostService ) {

        this.posts = postService.getPosts();
    }
}