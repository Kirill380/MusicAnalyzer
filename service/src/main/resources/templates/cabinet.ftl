<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Cabinet</title>
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/project/styles/music_list.css">
    <link rel="stylesheet" href="/project/styles/music_card.css">
    <link rel="stylesheet" href="/project/styles/music_cabinet.css">
</head>
<body>

<#include "header.ftl"/>
<div class="container cabinet">
    <div class="controls">
        <div class="control__item">
            <input type="text" class="form-control jsMusicFilter"
                   placeholder="Filter music by name">
        </div>
        <div class="control__item">
            <button type="button" class="btn btn-success jsAdd">Add +</button>
        </div>
    </div>
    <div class="music-list jsMusicList">
    </div>
    <nav aria-label="Page navigation">
        <ul class="pagination">
            <li>
                <a href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li><a href="#">1</a></li>
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>
            <li>
                <a href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
    <div class="jsPopUpMusic" style="display:none">
        <form action="/api/v1/user/musics" method="post">
            <div class="form-group">
                <label for="music">Music file: </label>
                <input type="file" class="form-control" name="music" id="music">
            </div>
            <input type="button" class="bClose jsAddMusic btn btn-default" value="create">
        </form>
    </div>
</div>


<script src="<@spring.url '/vendor/jquery-3.1.1.min.js'/>"></script>
<script src="<@spring.url '/vendor/bootstrap-3.3.7/js/bootstrap.js'/>"></script>
<script src="<@spring.url '/vendor/jquery.bpopup.min.js'/>"></script>
<script src="<@spring.url '/project/Cabinet.js' />"></script>
</body>
</html>