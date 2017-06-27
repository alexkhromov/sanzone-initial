import { Component } from '@angular/core';

export class Example {
    id: number;
    name: string;
    article: string;
    imagePath: string;
}

const EXAMPLES: Example[] = [
    { id: 1, name: 'Расчет санитарно-защитной зоны в горизонтальной плоскости антенн расположенных в одной точке пространства при одинаковых характеристиках антенн',article:'Данный расчет позволяет рассчитывать санитарно-защитную зону при расположении антенн в одной точке в горизонтальной плоскости, т.е. при совпадении координат (x, y, z) антенн.',       imagePath: 'http://res.cloudinary.com/sanzone/image/upload/v1498126296/1_kexgrw.jpg' },
    { id: 2, name: 'Расчет санитарно-защитной зоны в горизонтальной плоскости антенн расположенных в одной точке пространства при одинаковых характеристиках антенн', article:'', imagePath: 'http://res.cloudinary.com/sanzone/image/upload/v1498126296/2_rdpf7i.jpg' },
    { id: 3, name: 'Расчет в горизонтальной плоскости 3', imagePath: 'http://res.cloudinary.com/sanzone/image/upload/v1498126296/3_dpcjyw.jpg' },
    { id: 4, name: 'Расчет в горизонтальной плоскости 4', imagePath: 'http://res.cloudinary.com/sanzone/image/upload/v1498126296/4_eeklzm.jpg' },
    { id: 5, name: 'Расчет в горизонтальной плоскости 5', imagePath: 'http://res.cloudinary.com/sanzone/image/upload/v1498126296/5_csy2yi.jpg' },
    { id: 6, name: 'Расчет в горизонтальной плоскости 6', imagePath: 'http://res.cloudinary.com/sanzone/image/upload/v1498126296/6_z9sgpp.jpg' },
    { id: 7, name: 'Расчет в вертикальной плоскости', imagePath: 'http://res.cloudinary.com/sanzone/image/upload/v1498126296/7_e2lv6t.jpg' },
];

@Component({
    selector: 'exampleComponent',
    template: `
               <div class="container">
                   <ul *ngFor="let example of examples">
                     <div class="row">
                       <div class="col-md-7">
                         <p class="bg-primary">{{example.name}}</p>
                         <em>{{example.article}}</em> 
                       </div>
                       <div class="col-md-5">
                       <img class="img-responsive center-block" src={{example.imagePath}} alt="">
                       </div>
                     </div>
                     <hr>
                   </ul>
                 
               <!-- FOOTER -->
                 <footer>
                   <p class="pull-right"><a href="#">Вернуться к началу</a></p>
                   <p>&copy; 2017 EMFs. &middot; <a href="#">Конфиденциально</a>
                 </footer>
               </div>
               
               
             
  `,
    styleUrls: ['']
})
export class ExampleComponent {
    examples = EXAMPLES;
     }
}
