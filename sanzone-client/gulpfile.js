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
var uglify = require( 'gulp-uglify' );
var cssmin = require( 'gulp-cssmin' );
var rename = require( 'gulp-rename' );
var htmlmin = require( 'gulp-htmlmin' );
var strip = require( 'gulp-strip-comments' );
var tscConfig = require( './tsconfig.json' );
var Builder = require( 'systemjs-builder' );
var builder = new Builder( '.', './app/systemjs.config.js' );

var target = 'target';
var bundleHash = new Date().getTime();
var mainBundleName = bundleHash + '.app.bundle.js';
var vendorBundleName = bundleHash + '.vendor.bundle.js';

// clean up 'target' directory
gulp.task( 'pre:clean', function () {
    return del( target + '/**/*' );
} );

// clean up unnecessary artifacts
gulp.task( 'post:clean', [ 'inject' ], function () {
    return del( [ target + '/component/**', target + '/scripts/**', target + '/model/**',
                  target + '/service/**', target + '/systemjs.config.js' ] );
} );

// copy html
gulp.task( 'copy:html', [ 'pre:clean' ], function() {
    return gulp.src( [ 'app/**/*', '!app/**/*.ts' ] )
        .pipe( gulp.dest( target ) )
} );

// copy css
gulp.task( 'copy:css', [ 'pre:clean' ], function() {
    return gulp.src( [ 'node_modules/bootstrap/dist/css/bootstrap.css',
                       'node_modules/bootstrap/dist/css/bootstrap-theme.css' ] )
        .pipe( cssmin() )
        .pipe( rename( { extname: '.min.css' } ) )
        .pipe( gulp.dest( target + '/styles/css' ) )
} );

// copy fonts
gulp.task( 'copy:fonts', [ 'pre:clean' ], function() {
    return gulp.src( [ 'node_modules/bootstrap/dist/fonts/**' ] )
            .pipe( gulp.dest( target + '/styles/fonts' ) )
} );

// copy bootstrap js
gulp.task( 'copy:bootstrap-js', [ 'pre:clean' ], function() {
    return gulp.src( [ 'node_modules/bootstrap/dist/js/bootstrap.js' ] )
        .pipe( uglify() )
        .pipe( rename( { extname: '.min.js' } ) )
        .pipe( gulp.dest( target + '/styles/js' ) )
} );

gulp.task( 'bundle', [ 'bundle:vendor', 'bundle:app' ], function () {
    return gulp.src( target + '/index.html' )
        .pipe( htmlreplace( {
            'vendor': vendorBundleName,
            'app': mainBundleName
        } ) )
        .pipe( gulp.dest( target ) )
} );

gulp.task( 'bundle:app', [ 'bundle:vendor' ], function () {
    return builder
        .buildStatic( target + '/component/boot', target + '/lib/' + mainBundleName, { minify: true } )
        .catch( function( err ) {
            console.log( 'App bundle error');
            console.log( err );
        });
});

gulp.task( 'bundle:vendor', [ 'compile' ], function () {
    return builder
        .buildStatic( target + '/scripts/vendor', target + '/lib/' + vendorBundleName, { minify: true } )
        .catch( function( err ) {
            console.log( 'Vendor bundle error');
            console.log( err );
        });
});

// inject js libs
gulp.task( 'inject', [ 'bundle' ], function( callback ) {
    gulp.src( target + '/index.html' )
        .pipe( inject( series( gulp.src( target + '/lib/*.vendor.bundle.js', { read: false } ),
                               gulp.src( target + '/lib/*.app.bundle.js', { read: false } ),
                               gulp.src( target + '/styles/css/bootstrap.min.css', { read: false } ) ),
                       { ignorePath: target, addRootSlash: false } ) )
        .pipe( gulp.dest( target ) )
        .on( 'end', callback );
} );

// TypeScript compile
gulp.task( 'compile', [ 'copy' ], function () {
    return gulp.src( 'app/**/*.ts' )
        .pipe( sourcemaps.init() )
        .pipe( typescript( tscConfig.compilerOptions ) )
        .pipe( sourcemaps.write( '.' ) )
        .pipe( gulp.dest( target ) );
} );

gulp.task( 'minify:html', [ 'inject' ], function() {
    return gulp.src( target + '/**.html' )
        .pipe( strip() )
        .pipe( htmlmin( { collapseWhitespace: true } ) )
        .pipe( gulp.dest( target ) );
} );

gulp.task( 'default', [ 'build' ] );
gulp.task( 'copy', [ 'copy:html', 'copy:css', 'copy:fonts', 'copy:bootstrap-js' ] )
gulp.task( 'build', [ 'compile', 'bundle', 'inject', 'minify:html', 'post:clean' ] );