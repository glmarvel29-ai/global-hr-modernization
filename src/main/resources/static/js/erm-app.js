/**
 * AngularJS 1.8 frontend shell — Complexity-table stack (JSP + AngularJS).
 * LEGACY PROBLEM: directives and Spring MVC endpoints are tightly coupled
 * (High code dependencies / legacy frameworks).
 */
(function () {
    'use strict';

    var app = angular.module('ermApp', []);

    app.controller('DashboardCtrl', ['$http', function ($http) {
        var vm = this;
        vm.health = {};
        vm.risks = [];
        vm.boot = function () {
            $http.get('/api/health').then(function (res) {
                vm.health = res.data;
            });
            $http.get('/api/risks').then(function (res) {
                vm.risks = res.data;
            });
        };
    }]);

    app.controller('RisksCtrl', ['$http', function ($http) {
        var vm = this;
        vm.risks = [];
        vm.severityFilter = '';
        vm.boot = function () {
            $http.get('/api/risks').then(function (res) {
                vm.risks = res.data;
            });
        };
        vm.bySeverity = function (item) {
            if (!vm.severityFilter) {
                return true;
            }
            return item.severity === vm.severityFilter;
        };
    }]);

    app.controller('SecurityCtrl', ['$http', function ($http) {
        var vm = this;
        vm.posture = {};
        vm.boot = function () {
            $http.get('/api/security/posture').then(function (res) {
                vm.posture = res.data;
            });
        };
    }]);
})();
