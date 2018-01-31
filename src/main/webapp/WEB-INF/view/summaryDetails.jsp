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

        
    </div>
</body>

<script>
    angular.module('fupApp', [])
        .controller('fupController', function ($scope, $http) {

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
                        alert(d);
                    })
                    .error(function () {
                    });

        });
</script>
</html>