import {Component} from '@angular/core';

@Component( {

    selector: 'navbar',

    template: `
                <nav class="navbar navbar-default ">
                    <div class="container-fluid">
                         <!-- Brand and toggle get grouped for better mobile display -->
                              <div class="navbar-header">
                                  <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                                  <span class="sr-only">Toggle navigation</span>
                                      <span class="icon-bar"></span>
                                      <span class="icon-bar"></span>
                                      <span class="icon-bar"></span>
                                  </button>
                                  <a class="navbar-brand" href="#">Electro Magnetic Field Software
                                  <img class="logo-emfs" alt="logo-emfs" src="http://res.cloudinary.com/emfs/image/upload/v1491570457/logo-emfs_h4hrpj.svg">
                                  </a>
                              </div>
                    </div>
                </nav>
              `,


    styles: [
             `
             .navbar
                {
                 background-color:green;
                }
             .navbar-brand
                {
                 color: blue;
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