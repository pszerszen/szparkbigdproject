(function() {
    'use strict';
    angular
        .module('analyzerApp')
        .factory('Processor', Processor);

    Processor.$inject = ['$resource'];

    function Processor($resource) {
        var resourceUrl = 'http://10.10.31.220:8181/api/processor/';

        return $resource(resourceUrl, {}, {
            'getPossibleJobs': {
                method: 'GET',
                url: resourceUrl + 'possibleJobs'
            },

            'performJob': {
                method: 'GET',
                url: resourceUrl + 'performJob'
            }
        })
    }
})();
