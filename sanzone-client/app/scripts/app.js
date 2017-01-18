'use strict';

angular.module( 'Sanzone', [
  'Sanzone.controllers',
  'Sanzone.services',
  'Sanzone.config',
  'angularShamSpinner'
] );

  angular.module( 'Sanzone.controllers', [] ).
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
