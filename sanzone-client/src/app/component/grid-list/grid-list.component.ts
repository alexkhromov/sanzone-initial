import { Component, OnInit } from '@angular/core';
import { PostService } from '../../service/post.service';

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

@Component({
  selector: 'app-grid-list',
  templateUrl: './grid-list.component.html',
  styleUrls: ['./grid-list.component.css']
})
export class GridListComponent implements OnInit {
  posts;
  constructor(postService: PostService) { this.posts = postService.getPosts(); }

  ngOnInit() {
  }

  tiles: Tile[] = [
    { text: 'Расчет', cols: 3, rows: 1, color: 'lightblue' },
    { text: 'Формирование отчетов', cols: 1, rows: 2, color: 'lightgreen' },
    { text: 'Ипользование Goolle Maps', cols: 1, rows: 1, color: 'lightpink' },
    { text: 'И многое другое', cols: 2, rows: 1, color: '#DDBDF1' },
  ];



}
