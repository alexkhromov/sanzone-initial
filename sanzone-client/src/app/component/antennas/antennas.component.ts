import { Component, OnInit, Input } from '@angular/core';
import { AntennaService } from '../../service/antenna.service';
import { Antenna } from '../../model/antenna';



@Component({
    selector: 'my-antennas',
    templateUrl: './antennas.component.html',
    styleUrls: ['./antennas.component.css']
})

export class AntennasComponent implements OnInit {

    antenna: Antenna;
    status: boolean = false;
    heightM: number;
    azimuthM: number;
    sector: Array<any>;
    status1: any;
    panelOpenState = false;

    headers: string[];
    displayedColumns: string[] = ['latitude', 'longitude', 'azimuth', 'downTilt', 'height', 'antenna'];

    constructor(private configService: AntennaService,
    ) { }

    ngOnInit() {
        this.configService.getConfigResponse()
            // resp is of type `HttpResponse<Config>`
            .subscribe(resp => {
                // display its headers
                const keys = resp.headers.keys();
                this.headers = keys.map(key =>
                    `${key}: ${resp.headers.get(key)}`);

                // access the body directly, which is typed as `Config`.
                this.antenna = { ...resp.body };
                this.status = true;
                this.heightM = this.antenna.data[0].heightM;
                this.azimuthM = this.antenna.data[0].azimuthM;
                this.sector = this.antenna.data[0].sectors;
                //console.log(this.headers);
            });
    }

    showConfigResponse() {

        this.configService.getConfigResponse()
            // resp is of type `HttpResponse<Config>`
            .subscribe(resp => {
                // display its headers
                const keys = resp.headers.keys();
                this.headers = keys.map(key =>
                    `${key}: ${resp.headers.get(key)}`);

                // access the body directly, which is typed as `Config`.
                this.antenna = { ...resp.body };
            });
    }

    addAntenna() {
        console.log(this.heightM);
        this.configService.addAntenna({ 'heightM': this.heightM, 'azimuthM': this.azimuthM, 'sectors': this.sector })
            .subscribe(response => console.log(response));
    }




    
}

