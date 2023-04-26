<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="UTF-8">
    <title>小红书收藏</title>
    <link rel="stylesheet" href="https://unpkg.com/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css">
    <style>
        td {
            vertical-align: middle;
            font-weight: 700;
        }

        a {
            text-decoration: none;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col">
            <table class="table">
                <thead>
                <tr>
                    <th>封面图</th>
                    <th>笔记标题</th>
                    <th>笔记作者</th>
                    <th>笔记类型</th>
                    <th>点赞数</th>
                </tr>
                </thead>
                <tbody>
                <#list notes as note>
                    <tr>
                        <td><img src=${note.coverUrl}?imageView2/2/w/160/format/webp></td>
                        <td><a href=https://www.xiaohongshu.com/explore/${note.noteId} target="_blank">${note.displayTitle}</a></td>
                        <td><a href="https://www.xiaohongshu.com/user/profile/${note.ownerId}" target="_blank">${note.ownerNickname}</a></td>
                        <td>${note.type}</td>
                        <td>${note.likedCount}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>

</html>