
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Antenna } from '../../model/antenna';
import { AntennaService } from '../../service/antenna.service';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'my-antennas',
    templateUrl: './antennas.component.html',
    styleUrls: ['./antennas.component.css']
})

export class AntennasComponent implements OnInit {
    title = 'Параметры антенн';
    antennas: Antenna[];
    selectedAntenna: Antenna;
  private rooms:Array<{num:number,beds:number}>=[];

  private slogan:string='';

  user: any;

    constructor(private antennaService: AntennaService,
                private router: Router,
                private http: HttpClient,
                private http1:HttpClient,
                private http2:HttpClient) { }

    getAntennas(): void {
        this.antennaService
            .getAntennas()
            .subscribe(antennas => this.antennas = antennas);
    }

    add(name: string): void {
        name = name.trim();
        if (!name) { return; }
        this.antennaService.addAntenna({ name } as Antenna)
            .subscribe(antenna => {
                this.antennas.push(antenna);
                this.selectedAntenna = null;
            });
    }

    delete(antenna: Antenna): void {
      this.antennas = this.antennas.filter(h => h !== antenna);
      this.antennaService.deleteAntenna(antenna).subscribe();
    }

    ngOnInit(): void {
      this.http1
        .get('http://fe.it-academy.by/Examples/Hotel/rooms.json')
        .subscribe( (data)=>{
          console.log(data);
          this.rooms
            =<Array<{num:number,beds:number}>>data;
        } )
      ;
      this.http2
        .get('http://fe.it-academy.by/Examples/Hotel/slogan.txt',
          {responseType: 'text'})
        .subscribe( (data)=>{
          console.log(data);
          this.slogan=data;
        } )
      ;
    }

    onSelect(antenna: Antenna): void {
        this.selectedAntenna = antenna;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedAntenna.id]);
    }


  getData() {
    return  this.http.get('http://localhost:8081/v1/test').subscribe((data:any) => this.user=data);  }
}

