var map;
var colors = [];
colors[google.maps.TravelMode.BICYCLING] = '#00ff00';
colors[google.maps.TravelMode.DRIVING] = '#ff0000';
colors[google.maps.TravelMode.WALKING] = '#00ffff';
colors[google.maps.TravelMode.TRANSIT] = '#0000ff';

var renderers = [];

function initializeMap() {
    var mapElement = document.getElementById('route-map');
    if(mapElement) {
        var mapOptions = {
            center : new google.maps.LatLng(0, 0),
            zoom : 1,
            mapTypeId : google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(mapElement, mapOptions);
    }
}

function showRoutes(routes) {
    for ( var i = 0; i < renderers.length; i++) {
        renderers[i].setDirections({
            routes : []
        });
    }
    renderers = [];
    var results = {
        routes : []
    };
    for ( var i = 0; i < routes.length; i++) {
        requestDirections(routes[i]);
    }
}

function renderDirections(result, travelMode) {
    var directionsRenderer = new google.maps.DirectionsRenderer;
    directionsRenderer.setOptions({
        suppressMarkers : true,
        polylineOptions : {
            strokeColor : colors[travelMode],
            strokeWeight : 5,
            strokeOpacity : 0.50
        }
    });
    directionsRenderer.setMap(map);
    directionsRenderer.setDirections(result);
    renderers.push(directionsRenderer);
}

function requestDirections(route) {
    var directionsService = new google.maps.DirectionsService;
    var travelMode = google.maps.TravelMode[route.travelMode.toUpperCase()];
    directionsService.route({
        origin : route.origin,
        destination : route.destination,
        transitOptions : {
            arrivalTime : new Date(route.arrivalTime)
        },
        travelMode : travelMode
    }, function(result) {
        renderDirections(result, travelMode);
    });
}

function loadRoutes(location) {
    $('#routes').html('<h4>Calculating routes, hang on...</h4>' +
        '<div class="progress progress-striped active">' +
        '<div class="bar" style="width: 100%;"></div>' +
        '</div>');
    $.get("/authenticated/routes", {
        location : location
    }, function(html) {
        $("#routes").html(html);
        attachRouteMapListener();
    });
}

function attachRouteMapListener() {
    $('.route input[type="radio"]').change(function() {
        if ($(this).is(':checked')) {
            showRoutesFor($(this));
        }
    });

    $('.route input[type="radio"]:checked').each(function() {
        showRoutesFor($(this));
    });
}

function showRoutesFor(element) {
    var routeInfo = element.siblings('.route-data');
    var routes = [];
    routeInfo.children().each(function() {
        routes.push({
            origin : $(this).attr("data-origin"),
            destination : $(this).attr("data-destination"),
            arrivalTime : $(this).attr("data-arrival-time"),
            travelMode : $(this).attr("data-travel-mode")
        });
    });
    showRoutes(routes);
}

function s4() {
    return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
}

function uuid() {
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
}

function typeahead() {
    var url = $(this).attr('data-url');
    $(this).keyup(function() {
        $(this).validate();
    });
    $(this).change(function() {
        $(this).validate();
    });
    $(this).typeahead({
        source : function(query, process) {
            return $.get(url, {
                name : query
            }, function(data) {
                return process(data);
            });
        },
        items : 5
    });
}

function deleteNode() {
    var id = '#' + $(this).attr('data-ref');
    $(id).remove();
}

function success(message) {
    $('#notifications').html('<div class="alert alert-success">' + message + '</div>')
}

$(function() {

    $.validator.addMethod('regex', function(value, element, regexp) {
        var re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, 'Invalid input.');

    $.validator.setDefaults({
        ignore: []
    });

    initializeMap();

    var settingsValidator = $('#settings-form').validate({
        rules : {
            'preferences-homeLocationName' : {
                required : true
            },
            'preferences-organisationName' : {
                required : true
            }
        },
        highlight : function(label) {
            var controlGroup = $(label).closest('.control-group');
            controlGroup.addClass('error');
        },
        unhighlight : function(label) {
            var controlGroup = $(label).closest('.control-group');
            controlGroup.removeClass('error');
        },
        errorPlacement : function(error, element) {
            error.appendTo(element.closest('.control-group'));
        },
        onkeyup : false
    });

    var setupValidator = $('#setup-form').validate({
         rules : {
             'firstName' : {
                 required : true
             },
             'lastName' : {
                 required : true
             },
             'emailAddress' : {
                 required : true,
                 regex : '^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,4})$'
             },
             'password' : {
                 required : true
             },
             'passwordRepeat' : {
                 required : true,
                 equalTo: '[name="password"]'
             },
             'organisationName' : {
                 required : true
             },
             'organisationHQLocation' : {
                 required : true
             },
             'googleAPIKey' : {
                 required : true
             },
             'wundergroundAPIKey' : {
                 required : true
             }
         },
         highlight : function(label) {
             var controlGroup = $(label).closest('.control-group');
             controlGroup.addClass('error');
         },
         unhighlight : function(label) {
             var controlGroup = $(label).closest('.control-group');
             controlGroup.removeClass('error');
         },
         errorPlacement : function(error, element) {
             error.appendTo(element.closest('.control-group'));
         },
         onkeyup : false
    });

    attachRouteMapListener();

    $('form input, form select').keypress(function(e) {
        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
            $('button[type=submit]').click();
            return false;
        } else {
            return true;
        }
    });

    $('[data-provide="typeahead"]').each(typeahead);
    $('#settings').on('hidden', function() {
        $('#settings-form')[0].reset();
    });
    $('#settings').on('hidden', function() {
        $('#settings-form')[0].reset();
        settingsValidator.resetForm();
    });

    $('[data-action="delete"]').click(deleteNode);

    $('[data-action="add"]').click(function() {
        var id = '#' + $(this).attr('data-ref').replace(':', '\\:');
        var html = $('<div />').append($(id).clone()).html();
        var attributes = html.match(/:\w+/g);
        for(var i = 0; i < attributes.length; i++) {
            var parts = attributes[i].match(/:(\w+)/g);
            var name = parts[0];
            var expression = $(this).attr('data-' + name.substring(1));
            var value = eval(expression);
            while(html.indexOf(name) !== -1) {
                html = html.replace(attributes[i], value);
            }
        }
        $(id).parent().append(html);
        var newChild = $(id).siblings().last();
        newChild.removeClass('hidden');
        newChild.find('[data-provide="typeahead"]').each(typeahead);
        newChild.find('[data-action="delete"]').click(deleteNode);
    });
    
    $('#workLocation').change(function() {
        loadRoutes($(this).val());
    });
    
    $('#workLocationForm').submit(function() {
        $.post($(this).attr("action"), {
            workLocation : $('#workLocation').val()
        }, function() {
            success($('#workLocation option:selected').text() + ' is now your default work location.');
        });
        return false;
    });

    $('button[data-toggle="tab"]').click(function() {
        $('a[href="' + $(this).attr('href') + '"][data-toggle="tab"]').tab('show');
    });
});