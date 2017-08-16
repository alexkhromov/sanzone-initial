/**
 * System configuration for Angular samples
 * Adjust as necessary for your application needs.
 */
(function (global) {
    System.config({
        paths: {
            // paths serve as alias
            'npm:': './node_modules/',
            '*': '*.js'
        },
        // map tells the System loader where to look for things
        map: {
            component: '../target/component',
            service: '../target/service',
            vendor: '../target/scripts/vendor.js',

            // angular bundles
            '@angular/core': 'npm:@angular/core/bundles/core.umd.js',
            '@angular/common': 'npm:@angular/common/bundles/common.umd.js',
            '@angular/compiler': 'npm:@angular/compiler/bundles/compiler.umd.js',
            '@angular/platform-browser': 'npm:@angular/platform-browser/bundles/platform-browser.umd.js',
            '@angular/platform-browser-dynamic': 'npm:@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.js',
            '@angular/forms': 'npm:@angular/forms/bundles/forms.umd.js',
            '@angular/router': 'npm:@angular/router/bundles/router.umd.js',
            '@angular/common/http': 'npm:@angular/common/bundles/common-http.umd.js',
            '@angular/http': 'npm:@angular/http/bundles/http.umd.js',

            // other libraries
            'rxjs': 'npm:rxjs',
            'angular-in-memory-web-api': 'npm:angular-in-memory-web-api/bundles/in-memory-web-api.umd.js',
            'tslib': 'npm:tslib/tslib.js',

            //shims
            'core-js': 'npm:core-js/client/shim.min.js',
            'zone': 'npm:zone.js/dist/zone.js'
        },
        // packages tells the System loader how to load when no filename and/or no extension
        packages: {
            component: {
                format: 'register',
                defaultExtension: 'js'
            },
            service: {
                format: 'register',
                defaultExtension: 'js'
            },
            rxjs: {
                defaultExtension: 'js'
            }
        }
    });
})(this);