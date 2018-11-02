
import { Component, OnInit } from '@angular/core';
import { AntennaService } from '../../service/antenna.service';
import { Antenna } from '../../model/antenna';

@Component({
    selector: 'my-antennas',
    templateUrl: './antennas.component.html',
    styleUrls: ['./antennas.component.css']
})

export class AntennasComponent implements OnInit {

    antenna: Antenna;
    headers: string[];

    constructor(private configService: AntennaService,
    ) { }

    ngOnInit() {
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

