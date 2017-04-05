/**
 * Created by DEV on 16.03.2017.
 */
'use strict';

var gulp = require( 'gulp' );
var del = require( 'del' );
var typescript = require( 'gulp-typescript' );
var sourcemaps = require( 'gulp-sourcemaps' );
var htmlreplace = require( 'gulp-html-replace' );
var series = require( 'stream-series' );
var inject = require( 'gulp-inject' );
var tscConfig = require( './tsconfig.json' );
var Builder = require( 'systemjs-builder' );
var builder = new Builder( '.', './app/systemjs.config.js' );

var target = 'target';
var bundleHash = new Date().getTime();
var mainBundleName = bundleHash + '.app.bundle.js';
var vendorBundleName = bundleHash + '.vendor.bundle.js';

// clean the contents of the target directory
gulp.task( 'clean', function () {
    return del( target + '/**/*' );
} );

// copy dependencies
/*gulp.task( 'copy:lib', [ 'clean' ], function() {
    return gulp.src( [
        'node_modules/core-js/client/shim.min.js',
        'node_modules/zone.js/dist/zone.js',
        'node_modules/systemjs/dist/system.src.js',
        'node_modules/systemjs/dist/system-polyfills.js',
        'node_modules/rxjs/!**!/!*.js',
        'node_modules/@angular/core/bundles/core.umd.js',
        'node_modules/@angular/common/bundles/common.umd.js',
        'node_modules/@angular/compiler/bundles/compiler.umd.js',
        'node_modules/@angular/platform-browser/bundles/platform-browser.umd.js',
        'node_modules/@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.js',
        'node_modules/@angular/core/bundles/core.umd.js' ] )
        .pipe( gulp.dest( target + '/lib' ) )
} );*/

// copy html
gulp.task( 'copy:html', [ 'clean' ], function() {
    return gulp.src( [
        'app/**/*',
        '!app/**/*.ts' ] )
        .pipe( gulp.dest( target ) )
} );

// copy css
gulp.task( 'copy:css', [ 'clean' ], function() {
    return gulp.src( [
        'node_modules/bootstrap/dist/css/bootstrap.css' ] )
        .pipe( gulp.dest( target + '/styles' ) )
} );

gulp.task( 'bundle', [ 'bundle:vendor', 'bundle:app' ], function () {
    return gulp.src( target + '/index.html' )
        .pipe( htmlreplace( {
            'vendor': vendorBundleName,
            'app': mainBundleName,
        } ) )
        .pipe( gulp.dest( target ) )
} );

gulp.task( 'bundle:app', [ 'bundle:vendor' ], function () {
    return builder
        .bundle( target + '/component/boot', target + '/lib/' + mainBundleName )
        .catch( function( err ) {
            console.log( 'App bundle error');
            console.log( err );
        });
});

gulp.task( 'bundle:vendor', [ 'compile' ], function () {
    return builder
        .bundle( target + '/vendor', target + '/lib/' + vendorBundleName )
        .catch( function( err ) {
            console.log( 'Vendor bundle error');
            console.log( err );
        });
});

// inject js libs
gulp.task( 'inject', [ 'bundle' ], function() {
    gulp.src( target + '/index.html' )
        .pipe( inject( series( gulp.src( target + '/lib/*.vendor.bundle.js', { read: false } ),
                               gulp.src( target + '/lib/*.app.bundle.js', { read: false } ),
                               gulp.src( target + '/styles/*.css', { read: false } ) ),
                       { ignorePath: target, addRootSlash: false } ) )
        .pipe( gulp.dest( target ) );
} );

// TypeScript compile
gulp.task( 'compile', [ 'clean', 'copy:html', 'copy:css' ], function () {
    return gulp
        .src( 'app/**/*.ts' )
        .pipe( sourcemaps.init() )
        .pipe( typescript( tscConfig.compilerOptions ) )
        .pipe( sourcemaps.write( '.' ) )
        .pipe( gulp.dest( target ) );
} );

gulp.task( 'build', [ 'compile', 'bundle', 'inject' ] );
gulp.task( 'default', [ 'build' ] );