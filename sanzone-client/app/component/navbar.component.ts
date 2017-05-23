import {Component} from '@angular/core';

@Component( {

    selector: 'navbar',

    template: `<nav class="navbar navbar-default navbar-fixed-top">
                    <div class="container-fluid">
                              <div class="navbar-header">
                                  <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                                  <span class="sr-only">Toggle navigation</span>
                                      <span class="icon-bar"></span>
                                      <span class="icon-bar"></span>
                                      <span class="icon-bar"></span>
                                  </button>
                              </div> 
                              
                              <div class="brand-centered"> 
                                   <div class="navbar-brand" href="#">
                                   <img src="http://res.cloudinary.com/emfs/image/upload/v1491570457/logo-emfs_h4hrpj.svg" alt="logo">
                                   
                                   </div>
                              </div>
                              
                              <button type="button" class="btn btn-primary">login</button>
                    </div>
                </nav>
                
           
              `,


    styles: [
             `
             .navbar {
             background-color: #00BFFF; 
             }
             
             h5 {
             color: #FFFFFF;
             }
             
             .img{
             margin-right: 10px;
             padding: 0;
             }
             
             .btn {
                 float: right;
                 
             }  
                   
             .navbar-brand>img {
                 height: 40px;
                 /*padding: 0px;*/
                 width: auto;
             }
             
             .brand-centered {
             display: flex;
             justify-content: center;
             position: absolute;
             width: 100%;
             left: 0;
             top: 0;
             }
             
             .brand-centered .navbar-brand  {
             display: flex;
             align-items: center;
             }
             .navbar-toggle {
             z-index: 1;
             }
   
                              
                              
                              
                              
                              
                              
             `
            ]
         } )

export class NavbarComponent { }