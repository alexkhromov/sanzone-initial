import { Component, OnInit, Input } from '@angular/core';
import { AntennaService } from '../../service/antenna.service';
import { Antenna } from '../../model/antenna';

export interface PeriodicElement {
    name: string;
    position: number;
    weight: number;
    symbol: string;
    vv: string;
}
export interface AntennaElement {
    name: string;
    position: number;
    weight: number;
    symbol: string;
    vv: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
    { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H', vv: 'd' },
    { position: 2, name: 'Helium', weight: 4.0026, symbol: 'He', vv: 'd' },
    { position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li', vv: 'd' },
    { position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be', vv: 'd' },
    { position: 5, name: 'Boron', weight: 10.811, symbol: 'B', vv: 'd' },
    { position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C', vv: 'd' },
    { position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N', vv: 'd' },
    { position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O', vv: 'd' },
    { position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F', vv: 'd' },
    { position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne', vv: 'd' },
];

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
    displayedColumns: string[] = ['latitude', 'longitude', 'azimuth', 'downTilt', 'height', 'antenna']; //'power', 'gain', 'tractlost', 'earthfactor', 'diagramwidthwalfpowerH', 'diagramWwdthhalfpowerV'];
    dataSource = ELEMENT_DATA;

    constructor(private configService: AntennaService,
    ) { }

    // ngOnInit() {
    //     this.configService.getConfig()
    //         // clone the data object, using its known Config shape
    //         .subscribe((data: Antenna) => {
    //             this.antenna = { ...data };
    //             this.status = this.antenna.status;
    //             this.heightM = this.antenna.data[0].heightM;
    //             this.azimuthM = this.antenna.data[0].azimuthM;
    //             this.sector = this.antenna.data[0].sectors;

    //         });

    // }

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
                console.log(this.headers);
            });

    }





    showConfig() {
        this.configService.getConfig()
            // clone the data object, using its known Config shape
            .subscribe((data: Antenna) => this.antenna = { ...data });
        console.log(this.antenna);
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
        console.log(this.headers);
    }


}

