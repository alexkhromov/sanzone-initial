import { Component } from '@angular/core';
import { map } from 'rxjs/operators';
import { Breakpoints, BreakpointState, BreakpointObserver } from '@angular/cdk/layout';
import { PostService } from '../../service/post.service';
@Component({
  selector: 'app-dashboardcontent',
  templateUrl: './dashboardcontent.component.html',
  styleUrls: ['./dashboardcontent.component.css']
})
export class DashboardcontentComponent {
  /** Based on the screen size, switch from standard to one column per row */
  cards = this.breakpointObserver.observe(Breakpoints.Handset).pipe(
    map(({ matches }) => {
      if (matches) {
        return [
          { title: this.posts[0].title, cols: 4, rows: 3, color: 'lightblue', image: this.posts[0].image, article: this.posts[0].article, id: this.posts[0].id },
          { title: this.posts[1].title, cols: 4, rows: 3, color: 'lightgreen', image: this.posts[1].image, article: this.posts[1].article, id: this.posts[1].id },
          { title: this.posts[2].title, cols: 4, rows: 3, color: 'lightpink', image: this.posts[2].image, article: this.posts[2].article , id: this.posts[2].id},
          { title: this.posts[3].title, cols: 4, rows: 3, color: '#DDBDF1', image: this.posts[3].image, article: this.posts[3].article , id: this.posts[3].id},
          { title: this.posts[4].title, cols: 4, rows: 3, color: 'lightblue', image: this.posts[4].image, article: this.posts[4].article , id: this.posts[4].id},
          { title: this.posts[5].title, cols: 4, rows: 3, color: 'lightgreen', image: this.posts[5].image, article: this.posts[5].article , id: this.posts[5].id},
          { title: this.posts[6].title, cols: 4, rows: 3, color: 'lightpink', image: this.posts[6].image, article: this.posts[6].article , id: this.posts[6].id},

        ];
      }

      return [
        { title: this.posts[0].title, cols: 1, rows: 1, color: 'lightblue', image: this.posts[0].image, article: this.posts[0].article , id: this.posts[0].id},
        { title: this.posts[1].title, cols: 1, rows: 1, color: 'lightgreen', image: this.posts[1].image, article: this.posts[1].article , id: this.posts[1].id},
        { title: this.posts[2].title, cols: 1, rows: 1, color: 'lightpink', image: this.posts[2].image, article: this.posts[2].article , id: this.posts[2].id},
        { title: this.posts[3].title, cols: 1, rows: 1, color: '#DDBDF1', image: this.posts[3].image, article: this.posts[3].article , id: this.posts[3].id},
        { title: this.posts[4].title, cols: 1, rows: 1, color: 'lightblue', image: this.posts[4].image, article: this.posts[4].article, id: this.posts[4].id },
        { title: this.posts[5].title, cols: 1, rows: 1, color: 'lightgreen', image: this.posts[5].image, article: this.posts[5].article , id: this.posts[5].id},
        { title: this.posts[6].title, cols: 2, rows: 1, color: 'lightpink', image: this.posts[6].image, article: this.posts[6].article , id: this.posts[6].id},

      ];
    })
  );
  posts;

  constructor(private breakpointObserver: BreakpointObserver, postService: PostService) {
    this.posts = postService.getPosts();
  }

}
