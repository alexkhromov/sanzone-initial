'use strict';

module.exports = function ( grunt ) {

  grunt.loadNpmTasks( 'grunt-replace' );
  grunt.loadNpmTasks( 'grunt-sass' );

  // Time how long tasks take. Can help when optimizing build times
  require( 'time-grunt' )( grunt );

  // Automatically load required Grunt tasks
  require( 'jit-grunt' )( grunt, {
    useminPrepare: 'grunt-usemin',
    ngtemplates: 'grunt-angular-templates',
    cdnify: 'grunt-google-cdn'
  } );

  // Configurable paths for the application
  var appConfig = {
    app: require( './bower.json' ).appPath || 'app',
    dist: 'target'
  };

  // Define the configuration for all the tasks
  grunt.initConfig( {

    // Project settings
    yeoman: appConfig,

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      bower: {
        files: [ 'bower.json' ],
        tasks: [ 'wiredep' ]
      },
      sass: {
        files: [ 'sass/**/*.scss' ],
        tasks: [ 'sass' ],
        options: {
          livereload: '<%= connect.options.livereload %>'
        }
      },
      js: {
        files: [ '<%= yeoman.app %>/scripts/{,*/}*.js' ],
        tasks: [ 'newer:jshint:all', 'newer:jscs:all' ],
        options: {
          livereload: '<%= connect.options.livereload %>'
        }
      },
      gruntfile: {
        files: [ 'Gruntfile.js' ]
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [ '<%= yeoman.app %>/{,*/}*.html' ]
      }
    },

    // The actual grunt server settings
    connect: {
      options: {
        port: 9000,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        livereload: 35729
      },
      livereload: {
        options: {
          open: true,
          middleware: function ( connect ) {
            return [
              connect().use(
                '/bower_components',
                connect.static( './bower_components' )
              ),
              connect().use(
                '<%= yeoman.app %>/styles',
                connect.static( '<%= yeoman.app %>/styles' )
              ),

              connect.static( appConfig.app )
            ];
          }
        }
      },
      dist: {
        options: {
          open: true,
          base: '<%= yeoman.dist %>'
        }
      }
    },

    sass: {
      dist: {
        files: {
          '<%= yeoman.app %>/styles/spinner.css': 'sass/spinner.scss'
        }
      },
      options: {
        includePaths: [
          './bower_components'
        ]
      }
    },

    // Make sure there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require( 'jshint-stylish' )
      },
      all: {
        src: [
          'Gruntfile.js',
          '<%= yeoman.app %>/scripts/{,*/}*.js'
        ]
      }
    },

    // Make sure code styles are up to par
    jscs: {
      options: {
        config: '.jscsrc',
        verbose: true
      },
      all: {
        src: [
          'Gruntfile.js',
          '<%= yeoman.app %>/scripts/{,*/}*.js'
        ]
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
            '.tmp',
            '<%= yeoman.dist %>/{,*/}*',
            '!<%= yeoman.dist %>/.git{,*/}*'
          ]
        }]
      },
      server: '.tmp'
    },

    // Add vendor prefixed styles
    postcss: {
      options: {
        processors: [
          require( 'autoprefixer-core' )( { browsers: [ 'last 1 version' ] } )
        ]
      },
      server: {
        options: {
          map: true
        },
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '<%= yeoman.app %>/{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      },
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '<%= yeoman.dist %>/{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      }
    },


    // Automatically inject Bower components into the app
    wiredep: {
      app: {
        src: [ '<%= yeoman.app %>/**/*.html' ],
        ignorePath:  /\.\.\//
      }
    },

    // Renames files for browser caching purposes
    filerev: {
      dist: {
        src: [ '<%= yeoman.dist %>/scripts/{,*/}*.js' ]
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: '<%= yeoman.app %>/**/*.html',
      options: {
        dest: '<%= yeoman.dist %>',
        flow: {
          html: {
            steps: {
              js: [ 'concat', 'uglify' ],
              css: [ 'concat', 'cssmin' ]
            },
            post: {}
          }
        }
      }
    },

    // Performs rewrites based on filerev and the useminPrepare configuration
    usemin: {
      html: [ '<%= yeoman.dist %>/{,*/}*.html' ],
      js: [ '<%= yeoman.dist %>/scripts/{,*/}*.js' ],
      options: {
        assetsDirs: [
          '<%= yeoman.dist %>',
          '<%= yeoman.dist %>/scripts'
        ],
        patterns: {
          js: [ [ /(images\/[^''""]*\.(png|jpg|jpeg|gif|webp|svg))/g, 'Replacing references to images' ] ]
        }
      }
    },

    htmlmin: {
      dist: {
        options: {
          collapseWhitespace: true,
          conservativeCollapse: true,
          collapseBooleanAttributes: true,
          removeCommentsFromCDATA: true
        },
        files: [ {
          expand: true,
          cwd: '<%= yeoman.dist %>',
          src: [ '**/*.html' ],
          dest: '<%= yeoman.dist %>'
        } ]
      }
    },

    replace: {
      dev: {
        options: {
          patterns: [ {
            json: grunt.file.readJSON( './config/environments/dev.json' )
          } ]
        },
        files: [ {
          expand: true,
          flatten: true,
          src: [ './config/config.js' ],
          dest: '<%= yeoman.app %>/scripts/config/'
        } ]
      },
      local: {
        options: {
          patterns: [ {
            json: grunt.file.readJSON( './config/environments/local.json' )
          } ]
        },
        files: [ {
          expand: true,
          flatten: true,
          src: [ './config/config.js' ],
          dest: '<%= yeoman.app %>/scripts/config/'
        }]
      }
    },

    ngtemplates: {
      dist: {
        options: {
          module: 'Sanzone',
          htmlmin: '<%= htmlmin.dist.options %>',
          usemin: 'scripts/scripts.js'
        },
        cwd: '<%= yeoman.app %>',
        src: 'views/{,*/}*.html',
        dest: '.tmp/templateCache.js'
      }
    },

    // ng-annotate tries to make the code safe for minification automatically
    // by using the Angular long form for dependency injection.
    ngAnnotate: {
      dist: {
        files: [ {
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: '*.js',
          dest: '.tmp/concat/scripts'
        } ]
      }
    },

    // Replace Google CDN references
    cdnify: {
      dist: {
        html: [ '<%= yeoman.dist %>/**/*.html' ]
      }
    },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [ {
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>',
          src: [ '**/*.html' ]
        } ]
      }
    }
  } );


  grunt.registerTask( 'serve', 'Compile then start a connect web server', function ( target ) {

    var env = grunt.option( 'env' ) || 'dev';

    if ( target === 'dist' ) {
      return grunt.task.run( [ 'build', 'connect:dist:keepalive' ] );
    }

    grunt.task.run( [
      'clean:server',
      'wiredep',
      'postcss:server',
      'connect:livereload',
      'replace:' + env,
      'watch'
    ] );
  } );

  grunt.registerTask( 'build', [
    'clean:dist',
    'wiredep',
    'sass',
    'useminPrepare',
    'postcss',
    'ngtemplates',
    'concat',
    'ngAnnotate',
    'copy:dist',
    'cdnify',
    'cssmin',
    'uglify',
    'filerev',
    'usemin',
    'htmlmin'
  ] );

  grunt.registerTask( 'default', function() {

        var env = grunt.option( 'env' ) || 'dev';

        grunt.task.run( [
            'newer:jshint',
            'newer:jscs',
            'replace:' + env,
            'build'
        ] );
    } );
};
