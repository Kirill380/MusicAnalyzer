<#import "/spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User profile</title>
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/project/styles/profile.css">
</head>
<body>
<#include "header.ftl"/>
<div class="container profile_page">
    <div class="section">
        <h2 class="section__header">User info</h2>
        <form class="jsUserInfo" role="form" action="/api/v1/users/{userId}" method="PUT">
            <div class="form-group">
                <label for="inputEmail">Email</label>
                <input class="form-control" id="inputEmail" name="email" type="text" readonly>
            </div>
            <div class="form-group">
                <label for="inputFirstName">First name</label>
                <input class="form-control" id="inputFirstName" name="firstName" type="text" value="">
            </div>
            <div class="form-group">
                <label for="inputLastName">Last name</label>
                <input class="form-control" id="inputLastName" name="lastName" type="text" value="">
            </div>
            <div class="form-group">
                <input type="button" class="btn btn-success jsChangeUserInfo" value="Change">
            </div>
        </form>
    </div>

    <div class="section">
        <h2 class="section__header">Password change</h2>
        <form role="form" action="/api/v1/users/{userId}/password" method="PUT">
            <div class="form-group">
                <label for="inputPwdOld">Old password</label>
                <input class="form-control" id="inputPwdOld" name="oldPassword" type="password" value="">
            </div>
            <div class="form-group">
                <label for="inputPwdNew">New Password</label>
                <input class="form-control" id="inputPwdNew" name="newPassword" type="password" value="">
            </div>
            <div class="form-group">
                <input type="button" class="btn btn-success jsChangePassword"

                       value="Change">
            </div>
        </form>
    </div>

</div>
<script src="<@spring.url '/vendor/jquery-3.1.1.min.js'/>"></script>
<script src="<@spring.url '/vendor/bootstrap-3.3.7/js/bootstrap.js'/>"></script>
<script src="<@spring.url '/vendor/loading-indicator/jquery.loading-indicator.js'/>"></script>
<script src="<@spring.url '/vendor/jquery.bootstrap-growl.js'/>"></script>
<script src="<@spring.url '/project/js/NotifyUtil.js' />"></script>
<script src="<@spring.url '/project/js/Profile.js' />"></script>
</body>
</html>