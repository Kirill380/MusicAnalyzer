<#import "/spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign in</title>
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/project/styles/registration.css">
</head>
<body>
<div class="container registration-form">
    <div class="row">
        <div class="col-sm-7 col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong> Sign Up</strong>
                </div>
                <div class="panel-body">
                    <form role="form" action="/api/v1/users" method="POST">
                        <div class="row">
                            <div class="col-sm-12 col-md-10  col-md-offset-1 ">
                                <div class="form-group">
                                    <label for="inputEmail">Email</label>
                                    <input class="form-control"  id="inputEmail" placeholder="Email" name="email" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <label for="inputFirstName">First name</label>
                                    <input class="form-control" id="inputFirstName" placeholder="John" name="firstName" type="text" value="">
                                </div>
                                <div class="form-group">
                                    <label for="inputLastName">Last name</label>
                                    <input class="form-control" id="inputLastName" placeholder="Doe" name="lastName" type="text" value="">
                                </div>
                                <div class="form-group">
                                    <label for="inputPassword">Password</label>
                                    <input class="form-control"  id="inputPassword"  placeholder="Password" name="password" type="password" value="">
                                </div>
                                <div class="form-group">
                                    <input type="button" class="btn btn-lg btn-info btn-block jsRegistration" value="Sign up">
                                </div>
                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<@spring.url '/vendor/jquery-3.1.1.min.js'/>"></script>
<script src="<@spring.url '/vendor/bootstrap-3.3.7/js/bootstrap.js'/>"></script>
<script src="<@spring.url '/vendor/jquery.bootstrap-growl.js'/>"></script>
<script src="<@spring.url '/project/js/NotifyUtil.js' />"></script>
<script src="<@spring.url '/project/js/Registration.js' />"></script>
</body>
</html>