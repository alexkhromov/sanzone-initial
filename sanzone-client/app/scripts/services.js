'use strict';

angular.module( 'Sanzone.services', [ 'Sanzone.config' ] ).
  factory( 'sanzoneAPIservice', function( $http, configuration ) {

    var sanzoneAPI = {};

    sanzoneAPI.test = function() {
      return $http( {
        url: configuration.baseUrl + '/v1/test',
        method: 'GET'
      } );
    };

    sanzoneAPI.createSummarySanzone = function( $scope, configuration ) {

    return $http( {
        url: configuration.baseUrl + '/v1/summary',
        method: 'POST',
        data: { 'heightM' : $scope.heightM, 'azimuthM' : $scope.azimuthM, 'sectors' : $scope.sectors },
        headers: { 'Content-Type': 'application/json' }
      } );
    };

    return sanzoneAPI;
  } );
