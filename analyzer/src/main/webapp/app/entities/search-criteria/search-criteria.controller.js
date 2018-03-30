(function() {
    'use strict';

    angular
        .module('analyzerApp')
        .controller('SearchCriteriaController', SearchCriteriaController);

    SearchCriteriaController.$inject = ['$scope', '$state', 'SearchCriteria'];

    function SearchCriteriaController ($scope, $state, SearchCriteria) {
        var vm = this;
        vm.searchCriteria = [];
        vm.loadAll = function() {
            SearchCriteria.query(function(result) {
                vm.searchCriteria = result;
            });
        };

        vm.loadAll();
        
    }
})();
