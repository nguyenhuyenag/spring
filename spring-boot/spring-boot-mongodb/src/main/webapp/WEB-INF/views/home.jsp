<%@ page contentType="text/html" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <title>MongoDB</title>
        <link rel="shortcut icon" href="#">
        <link rel="icon" href="//spring.io/images/favicon-9d25009f65637a49ac8d91eb1cf7b75e.ico" type="image/x-icon">
        <!-- css -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                counter-reset: section;
                background-color: #f8f9fa !important;
            }

            h3::before {
                counter-increment: section;
                content: ""counter(section) ") ";
            }
        </style>
    </head>

    <body>
        <div class="container">
            <h1>MongoDB</h1>
            <h3>Spring JPA</h3>
            <ol>
                <li><a href="jpa/find-all" target="_blank">Find all</a></li>
                <li><a href="jpa/find-all-sort-by-word" target="_blank">Find all and sort by word</a></li>
            </ol>
            <h3>MongoTemplate</h3>
            <ol>
                <li><a href="jpa/find-all" target="_blank">Find all</a></li>
                <li><a href="jpa/find-all-sort-by-word" target="_blank">Find all and sort by word</a></li>
            </ol>
        </div>
    </body>

    </html>