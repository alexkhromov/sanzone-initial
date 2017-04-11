import {Component} from '@angular/core';

@Component( {

    selector: 'navbar',

    template: `
                <nav class="navbar navbar-default">
                    <div class="container-fluid">
                         <div class="navbar-header">
                              <a class="navbar-brand" href="#">Electro Marnetic Field Software
                              <img class="logo-emfs" alt="Brand" src="http://res.cloudinary.com/emfs/image/upload/v1491570457/logo-emfs_h4hrpj.svg">
                              </a>
                              <button type="button" class="btn btn-default navbar-btn pull-right">Sign in</button>
                         </div>
                    </div>
                </nav>
              `,


    styles: [
             `
             .navbar
                {
                 background-color:transparent;
                }
                
             .logo-emfs
                {
                 position: absolute;
	             top: 0;
	             bottom: 0;
	             left: 0;
	             right: 0;
	             height: 80%;
	             margin: auto;
                }
                
             .btn
                {
                 float: right;
                }                
             `
            ]
         } )

export class NavbarComponent { }