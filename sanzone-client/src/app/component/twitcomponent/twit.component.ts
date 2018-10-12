/**
 * Created by DEV on 13.03.2017.
 */
import { Component } from '@angular/core';
import { PostService } from '../../service/post.service';

@Component({

    selector: 'twit',

    templateUrl: './twit.component.html',
})

export class TwitComponent {

    posts;

    constructor(postService: PostService) {

        this.posts = postService.getPosts();
    }
}