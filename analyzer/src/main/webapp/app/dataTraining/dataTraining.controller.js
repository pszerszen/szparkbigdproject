(function() {
    'use strict';

    angular
        .module('analyzerApp')
        .controller('DataTrainingCtrl', DataTrainingCtrl);

    DataTrainingCtrl.$inject = ['$scope', '$state', 'Result', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Document'];

    function DataTrainingCtrl($scope, $state, Result, ParseLinks, AlertService, pagingParams, paginationConstants, Document){
        var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.performRating = performRating;
        vm.loadAll();

        function loadAll () {
            Result.query({
                page: pagingParams.page - 1,
                size: paginationConstants.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.results = data;
                vm.page = pagingParams.page;
                requestDocumentsFromResults(vm.results);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function requestDocumentsFromResults(results){
            var documentsIds = _.map(results, function(result){
                return result.documentId;
            });
            Document.listByIds({documentsIds: documentsIds}).$promise
                .then(function(result){
                    vm.documents = result;
                }, function(err){});
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function performRating(resultId, rating, documentId){
            var result = angular.copy(_.find(vm.results, function(result){return result.id = resultId}));
            result.classification = rating;
            result.isTrainingData = true;
            result.documentId = documentId;
            Result.update(result).$promise.then(function(updatedResult, headers){
                loadAll();
            }, function(err){});
        }
    }
})();
