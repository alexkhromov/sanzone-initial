/**
 * Created by DEV on 16.03.2017.
 */
'use strict';

var gulp = require( 'gulp' );
var del = require( 'del' );
var typescript = require( 'gulp-typescript' );
var sourcemaps = require( 'gulp-sourcemaps' );
var inject = require( 'gulp-inject' );
var tscConfig = require( './tsconfig.json' );

var target = 'target';

// clean the contents of the target directory
gulp.task( 'clean', function () {
    return del( target + '/**/*' );
} );

// copy dependencies
gulp.task( 'copy:lib', [ 'clean' ], function() {
    return gulp.src( [
        'node_modules/core-js/client/shim.min.js',
        'node_modules/systemjs/dist/system-polyfills.js',
        'node_modules/systemjs/dist/system.src.js',
        'node_modules/reflect-metadata/Reflect.js',
        'node_modules/rxjs/**/*.js',
        'node_modules/zone.js/dist/**',
        'node_modules/@angular/**/bundles/**' ] )
        .pipe( gulp.dest( target + '/lib' ) )
} );

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

// inject js libs
gulp.task( 'inject', [ 'copy:lib', 'copy:html' ], function() {
    gulp.src( target + '/index.html' )
        .pipe( inject( gulp.src( target + '/lib/*.js', { read: false } ),
                       { ignorePath: target, addRootSlash: false } ) )
        .pipe( inject( gulp.src( target + '/styles/*.css', { read: false } ),
                       { ignorePath: target, addRootSlash: false } ) )
        .pipe( gulp.dest( target ) );
} );

// TypeScript compile
gulp.task( 'compile', [ 'clean', 'copy:lib', 'copy:html', 'copy:css', 'inject' ], function () {
    return gulp
        .src( 'app/**/*.ts' )
        .pipe( sourcemaps.init() )
        .pipe( typescript( tscConfig.compilerOptions ) )
        .pipe( sourcemaps.write( '.' ) )
        .pipe( gulp.dest( target ) );
} );

gulp.task( 'build', [ 'compile' ] );
gulp.task( 'default', [ 'build' ] );