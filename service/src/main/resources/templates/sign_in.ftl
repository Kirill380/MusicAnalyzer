<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign in</title>
    <link rel="stylesheet" href="/project/css/sign-in.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap-theme.css">
</head>
<body>
<div class="container sign-in-form">
    <div class="row">
        <div class="col-sm-7 col-md-5 col-md-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong> Sign in </strong>
                </div>
                <div class="panel-body">
                    <form role="form" action="/login" method="POST">
                        <div class="row">
                            <div class="col-sm-12 col-md-10  col-md-offset-1 ">
                                <div class="form-group">
                                    <div class="input-group">
												<span class="input-group-addon">
													<i class="glyphicon glyphicon-user"></i>
												</span>
                                        <input class="form-control" placeholder="E-mail" name="username" type="text"
                                               autofocus>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
												<span class="input-group-addon">
													<i class="glyphicon glyphicon-lock"></i>
												</span>
                                        <input class="form-control" placeholder="Password" name="password"
                                               type="password" value="">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="submit" class="btn btn-lg btn-success btn-block" value="Sign in">
                                </div>
                            </div>
                        </div>
                        <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->

                    </form>
                </div>
                <div class="panel-footer ">
                    <#--Don't have an account! <a href="#" onClick=""> Sign Up Here </a>-->
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>