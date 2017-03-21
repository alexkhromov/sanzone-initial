import {Component} from '@angular/core';

@Component( {

    selector: 'navbar',

    template: `
                <nav class="navbar navbar-default">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <a class="navbar-brand" href="#">
                                <img alt="Brand" src="./logo_EMFs.png">
                            </a>
                        </div>
                    </div>
                </nav>`
} )

export class NavbarComponent { }