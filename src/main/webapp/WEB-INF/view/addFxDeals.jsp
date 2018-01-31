<html>
<head>
  <title>FX Deals File Upload</title>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.min.js"></script>
</head>

<body ng-app="fupApp">

<link href="/css/app.css" rel="stylesheet"></link>
 <meta name="viewport" content="width=device-width">
<div class="wrap">
<span class="decor"></span>
<nav>
  <ul class="primary">
    <li>
      <a href="/">Upload</a>
    </li>
    <li>
      <a href="/summaryReport">Summary Report</a>
      
    </li>
    <li>
      <a href="/generateSampleDeals">Generate Sample File</a>
      
    </li>
  </ul>
</nav>
</div>

    <div ng-controller="fupController">

        <input type="file" id="dealFile" name="dealFile" multiple
            ng-files="getTheFiles($files)" />

        <input type="button" ng-click="uploadFiles()" value="Upload" />
    </div>
</body>

<script>
    angular.module('fupApp', [])
        .directive('ngFiles', ['$parse', function ($parse) {

            function fn_link(scope, element, attrs) {
                var onChange = $parse(attrs.ngFiles);
                element.on('change', function (event) {
                    onChange(scope, { $files: event.target.files });
                });
            };

            return {
                link: fn_link
            }
        } ])
        .controller('fupController', function ($scope, $http) {

            var formdata = new FormData();
            $scope.getTheFiles = function ($files) {
                angular.forEach($files, function (value, key) {
                    formdata.append("dealFile", value);
                });
            };

            // NOW UPLOAD THE FILES.
            $scope.uploadFiles = function () {

                var request = {
                    method: 'POST',
                    url: '/addFxDeals',
                    data: formdata,
                    headers: {
                        'Content-Type': undefined
                    }
                };

                // SEND THE FILES.
                $http(request)
                    .success(function (d) {
                        alert("Upload Successfully");
                    })
                    .error(function () {
                    });
            }
        });
</script>
</html>