'use strict';

angular.module( 'F1FeederApp', [
  'F1FeederApp.controllers',
  'F1FeederApp.services',
  'F1FeederApp.config',
  'angularShamSpinner'
] );

  angular.module( 'F1FeederApp.controllers', [] ).
  controller( 'sectorsController', function( $scope, sanzoneAPIservice, configuration ) {
    
    $scope.environment = null;
    $scope.heightM = null;
    $scope.azimuthM = null;
    $scope.status = null;
    $scope.sectors = [];

    $scope.inProcess=false;

    $scope.environment = configuration.baseUrl;

    sanzoneAPIservice.test().then( function ( response ) {
        
        $scope.status = response.status;
        $scope.heightM = response.data.data[ 0 ].heightM;
        $scope.azimuthM = response.data.data[ 0 ].azimuthM;
        $scope.sectors = response.data.data[ 0 ].sectors;
    } );

    $scope.createSummarySanzone = function() {

       $scope.inProcess=true;
      sanzoneAPIservice.createSummarySanzone( $scope, configuration )
            .then( function( response ) {
               $scope.inProcess=false;
               $scope.status = response.status;
            } );
    };
  } );