/**
 * AngularJS 1.8 frontend shell — Complexity-table stack (JSP + AngularJS).
 * LEGACY PROBLEM: directives and Spring MVC endpoints are tightly coupled
 * (High code dependencies / legacy frameworks).
 */
(function () {
    'use strict';

    var app = angular.module('ermApp', []);

    app.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.defaults.withCredentials = true;
        $httpProvider.interceptors.push(['$q', '$window', function ($q, $window) {
            return {
                responseError: function (rejection) {
                    if (rejection.status === 401) {
                        $window.location.href = '/login';
                    } else if (rejection.status === 403) {
                        rejection.permissionMessage = 'You do not have permission to view this data.';
                    } else if (rejection.status === 400) {
                        rejection.validationMessage = rejection.data && rejection.data.message
                            ? rejection.data.message
                            : 'Invalid request';
                    }
                    return $q.reject(rejection);
                }
            };
        }]);
    }]);

    app.controller('DashboardCtrl', ['$http', function ($http) {
        var vm = this;
        vm.health = {};
        vm.risks = [];
        vm.errorMessage = '';
        vm.boot = function () {
            $http.get('/api/health').then(function (res) {
                vm.health = res.data;
            });
            $http.get('/api/risks').then(function (res) {
                vm.risks = res.data;
            }).catch(function (err) {
                vm.errorMessage = err.permissionMessage || err.validationMessage || 'Unable to load risks';
            });
        };
    }]);

    app.controller('RisksCtrl', ['$http', function ($http) {
        var vm = this;
        vm.risks = [];
        vm.severityFilter = '';
        vm.errorMessage = '';
        vm.boot = function () {
            $http.get('/api/risks').then(function (res) {
                vm.risks = res.data;
            }).catch(function (err) {
                vm.errorMessage = err.permissionMessage || err.validationMessage || 'Unable to load risks';
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
        vm.errorMessage = '';
        vm.boot = function () {
            $http.get('/api/security/posture').then(function (res) {
                vm.posture = res.data;
            }).catch(function (err) {
                vm.errorMessage = err.permissionMessage || err.validationMessage || 'Unable to load posture';
            });
        };
    }]);
})();
