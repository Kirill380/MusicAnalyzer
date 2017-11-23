<#import "/spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign in</title>
    <link rel="stylesheet" href="/project/styles/sign-in.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap-theme.css">
</head>
<body>
<div class="container sign-in-form">
    <div class="row">
        <div class="col-sm-7 col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong> Sign in </strong>
                </div>
                <div class="panel-body">
                    <form role="form" action="/auth/login" method="POST">
                        <div class="row">
                            <div class="col-sm-12 col-md-10  col-md-offset-1 ">
                                <div class="form-group">
                                    <div class="input-group">
												<span class="input-group-addon">
													<i class="glyphicon glyphicon-user"></i>
												</span>
                                        <input class="form-control" placeholder="E-mail" name="email" type="text"
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
                                    <input type="button" class="btn btn-lg btn-success btn-block jsLogin" value="Sign in">
                                </div>
                            </div>
                        </div>
                        <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->

                    </form>
                </div>
                <div class="panel-footer ">
                    Don't have an account! <a href="/registration"> Sign Up Here </a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<@spring.url '/vendor/jquery-3.1.1.min.js'/>"></script>
<script src="<@spring.url '/vendor/bootstrap-3.3.7/js/bootstrap.js'/>"></script>
<script src="<@spring.url '/vendor/jquery.bootstrap-growl.js'/>"></script>
<script src="<@spring.url '/project/js/NotifyUtil.js' />"></script>
<script src="<@spring.url '/project/js/Login.js' />"></script>
</body>
</html>